package com.example.prepay.ui.MyPage

import android.content.Context
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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentMyPageBinding
import com.example.prepay.ui.GroupDetails.GroupDetailsFragment
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

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
        initAdapter()
        initEvent()

        // 테스트 버튼 클릭 시 QR 코드 다이얼로그 호출
        view.findViewById<View>(R.id.test_button).setOnClickListener {
            showQRDialog()
        }
    }

    private fun initAdapter() {
        cardAdapter = TeamCardAdapter(arrayListOf())
        binding.viewPager.adapter = cardAdapter
        viewModel.teamListInfo.observe(viewLifecycleOwner) { teamList ->
            cardAdapter.teamList = teamList
            if (cardAdapter.teamList.isNotEmpty()) {
                binding.viewPager.setCurrentItem(cardAdapter.itemCount - 1, false)
            }
            cardAdapter.notifyDataSetChanged()
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
        binding.viewPager.setCurrentItem(0, false)
    }

    private fun initEvent() {
        binding.createGroupBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT)
        }
        binding.enterGroupBtn.setOnClickListener {
            mainActivity.enterDialog()
        }
        mainActivity.hideBottomNav(false)
    }

    /**
     * QR 코드를 생성하여 다이얼로그로 표시하는 함수
     *
     * @param context 다이얼로그를 표시할 컨텍스트 (Fragment 내에서는 requireContext()를 사용)
     * @param url QR 코드에 인코딩할 URL (기본값: "https://www.naver.com")
     */
    fun showQRDialog(url: String ="https://www.naver.com") {
        val context = this@MyPageFragment.requireContext()

        // 1. 다이얼로그 레이아웃 인플레이트
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_qr, null)

        // 2. QR 코드 생성 및 이미지뷰에 적용
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode = dialogView.findViewById<ImageView>(R.id.imageViewQrCode)
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("QRDialog", "QR 코드 생성 실패", e)
        }

        // 3. AlertDialog 생성
        val dialog = androidx.appcompat.app.AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)  // 뒤로 가기 버튼 허용
            .create()

        // 4. 다이얼로그 배경 투명화
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // ✅ 다이얼로그 배경 투명화
        dialog.show()
    }
}
