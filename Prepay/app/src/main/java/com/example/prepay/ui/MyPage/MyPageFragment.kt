package com.example.prepay.ui.MyPage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
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
            showQRDialog(this)
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
    fun showQRDialog(fragment: MyPageFragment, url: String ="https://www.naver.com") {
        val context = fragment.requireContext()
        val parentView = fragment.requireView()

        // 1. 기존 배경 저장 (복구용)
        val originalBackground = parentView.background

        // 2. 부모 프래그먼트 배경을 변경 (반투명 효과)
        parentView.setBackgroundColor(Color.parseColor("#990066CC"))  // ✅ 다이얼로그 스타일의 배경 적용

        // 3. 다이얼로그 레이아웃 인플레이트
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_create_qr, null)

        // 4. QR 코드 생성 및 이미지뷰에 적용
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 400, 400)
            val imageViewQrCode = dialogView.findViewById<ImageView>(R.id.imageViewQrCode)
            imageViewQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("QRDialog", "QR 코드 생성 실패", e)
        }

        // 5. AlertDialog 생성
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)  // 뒤로 가기 버튼 허용
            .create()

        // 6. 다이얼로그 닫힐 때 배경 복구
        dialog.setOnDismissListener {
            parentView.background = originalBackground  // ✅ 원래 배경으로 복구
        }

        // 7. 다이얼로그 표시
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // ✅ 다이얼로그 배경 투명화
        dialog.show()
    }
}
