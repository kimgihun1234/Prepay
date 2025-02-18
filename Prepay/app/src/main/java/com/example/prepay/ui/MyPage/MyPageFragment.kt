package com.example.prepay.ui.MyPage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.databinding.FragmentMyPageBinding
import com.example.prepay.ui.GroupDetails.GroupDetailsFragment
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

private const val TAG = "MyPageFragment"

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(
    FragmentMyPageBinding::bind,
    R.layout.fragment_my_page
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var cardAdapter: TeamCardAdapter
    private val viewModel: MyPageFragmentViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = requireActivity() as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "SharedPreferencesUtil.getNickName(): ${SharedPreferencesUtil.getNickName()}")
        binding.userName.text = "${SharedPreferencesUtil.getNickName()} 님"
        initAdapter()
        initEvent()
    }

    private fun initAdapter() {
        cardAdapter = TeamCardAdapter(arrayListOf())
        binding.viewPager.adapter = cardAdapter

        viewModel.teamListInfo.observe(viewLifecycleOwner) { teamList ->
            cardAdapter.teamList = teamList
            cardAdapter.notifyDataSetChanged()

            // 데이터가 있고, 최초 로드일 경우에만 마지막 카드로 이동
            if (teamList.isNotEmpty()) {
                binding.viewPager.setCurrentItem(cardAdapter.itemCount - 1, false)
                Log.d(TAG, "뭘까? ${cardAdapter.itemCount - 1}")
            }

        }
        viewModel.getAllTeamList()
        
        cardAdapter.itemClickListener = object : TeamCardAdapter.ItemClickListener {
            override fun onClick(teamId: Int) {
                activityViewModel.setTeamId(teamId.toLong())
                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.GROUP_DETAILS_FRAGMENT)
            }
        }
        // 스택 효과 추가
        binding.viewPager.setPageTransformer(StackPageTransformer())
        binding.viewPager.offscreenPageLimit = 5
    }

    private fun initEvent() {
        binding.createGroupBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_GROUP_FRAGMENT)
        }
        binding.enterGroupBtn.setOnClickListener {
            mainActivity.enterDialog()
        }
//        binding.groupDetailBtn.setOnClickListener {
//            val currentPosition = binding.viewPager.currentItem
//            val selectedTeam = cardAdapter.teamList.getOrNull(currentPosition)
//            selectedTeam?.let {
//                activityViewModel.setTeamId(it.teamId.toLong())
//                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.GROUP_DETAILS_FRAGMENT)
//            }
//        }

        binding.payBtn.setOnClickListener {
            val currentPosition = binding.viewPager.currentItem
            val selectedTeam = cardAdapter.teamList.getOrNull(currentPosition)
            lifecycleScope.launch {
                runCatching {
                    RetrofitUtil.qrService.qrPrivateCreate(SharedPreferencesUtil.getAccessToken()!!,selectedTeam!!.teamId)
                }.onSuccess {
                    showQRDialog(it.message+":"+ SharedPreferencesUtil.getAccessToken()!!+":"+selectedTeam!!.teamId.toString())
                }.onFailure {
                    mainActivity.showToast("qr불러오기가 실패했습니다")
                }
            }
        }


        mainActivity.hideBottomNav(false)
    }

    fun showQRDialog(url: String = "https://www.naver.com") {
        val context = this@MyPageFragment.requireContext()

        // 타이머 생성
        val timer = Timer()

        // 다이얼로그 레이아웃 인플레이트
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_qr, null)

        // QR 코드 생성 및 이미지뷰에 적용
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode = dialogView.findViewById<ImageView>(R.id.imageViewQrCode)
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("QRDialog", "QR 코드 생성 실패", e)
        }

        // AlertDialog 생성
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)  // 뒤로 가기 버튼 허용
            .create()


        // 다이얼로그 닫힐 때 타이머 취소
        dialog.setOnDismissListener {
            timer.cancel()
        }

        // 다이얼로그 표시
        dialog.show()

        // 60초 카운트다운 타이머 시작
        var seconds = 60
        val qrTimer = dialogView.findViewById<TextView>(R.id.qr_timer)
        qrTimer?.text = "남은 시간: 60초"

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                seconds--
                // UI 업데이트는 메인 스레드에서 수행
                this@MyPageFragment.requireActivity().runOnUiThread {
                    qrTimer?.text = "남은 시간: ${seconds}초"
                }
                if (seconds <= 0) {
                    // 시간이 다 되었으면 다이얼로그를 닫고 타이머 취소
                    this@MyPageFragment.requireActivity().runOnUiThread {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                    timer.cancel()
                }
            }
        }, 1000, 1000)
    }
}
