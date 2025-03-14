package com.example.qrscanner.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.RetrofitUtil
import com.example.qrscanner.MainActivity
import com.example.qrscanner.MainActivityViewModel
import com.example.qrscanner.R
import com.example.qrscanner.base.BaseFragment
import com.example.qrscanner.databinding.FragmentStoreTeamBinding
import com.example.qrscanner.response.OrderDetail
import com.example.qrscanner.response.PosReq
import com.example.qrscanner.ui.adapter.TeamAdapter
import com.example.qrscanner.ui.viewModel.TeamViewModel
import com.example.qrscanner.util.CommonUtils
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch

private const val TAG = "StoreTeamFragment"
class StoreTeamFragment : BaseFragment<FragmentStoreTeamBinding>(
    FragmentStoreTeamBinding::bind,
    R.layout.fragment_store_team
), TeamAdapter.OnButtonClick {

    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var teamAdapter : TeamAdapter
    private val teamViewModel : TeamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initAdapter()
    }

    private fun initEvent() {
        binding.qrCodeBtn.setOnClickListener {
            startQRCodeScanner()
            Log.d(TAG, "initEvent: ${activityViewModel.storeId.value}")
        }
    }

    private fun initAdapter() {

        binding.recyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        teamAdapter = TeamAdapter(arrayListOf(), onButtonClick = this)
        binding.recyclerView.adapter = teamAdapter
        teamViewModel.teamList.observe(viewLifecycleOwner) {teamList->
            teamAdapter.teamList = teamList
            teamAdapter.notifyDataSetChanged()
            Log.d(TAG, "teamList: ${teamList}")
        }
        activityViewModel.storeId.value?.let { teamViewModel.getTeamList(it) }
        val storeId = activityViewModel.storeId
        Log.d(TAG, "initAdapter: ${storeId.value}")
        Log.d(TAG, "initAdapter: 성공")
    }

    // ✅ 수정: Fragment에서 onActivityResult가 동작하도록 변경
    fun startQRCodeScanner() {
        Log.d(TAG, "startQRCodeScanner: ")
        val integrator = IntentIntegrator.forSupportFragment(this) // ✅ 변경된 부분
        integrator.setPrompt("Scan a QR code")
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
        Log.d(TAG, "startQRCodeScanner: 완료")
    }


    fun handleQRCodeScanResult(scanResult: String) {
        // QR 코드 데이터 처리
        Log.d("QR_SCAN", "QR 성공코드가 찍혔습니다: $scanResult")
        playBeepSound()
        val parts = scanResult.split(":")
        Log.d(TAG, "parts: ${parts[1]}")
        // 주문 상세 정보 리스트 생성
        val orderDetails = listOf(
            OrderDetail(detailPrice = 1000, product = "커피", quantity = 2),
            OrderDetail(detailPrice = 500, product = "샌드위치", quantity = 1)
        )
        //숫자 입력
        var num = activityViewModel.storeId.value
        Log.d(TAG, "storeId 값: $num") // ✅ storeId 값 확인
        // PosReq 객체 생성
        val posReq = num?.let {
            PosReq(
                details = orderDetails,
                qrUUID = parts[0],
                storeId = it,
                teamId = parts[2].toInt()
            )
        }
        Log.d(TAG, "posReq 생성 완료: $posReq") // ✅ PosReq 객체가 정상 생성되었는지 확인
        lifecycleScope.launch {
            runCatching {
                posReq?.let { RetrofitUtil.posService.posTransfer(parts[1], it) }
            }.onSuccess {
                Log.d("QR_SCAN","아이디어가 작동하였습니다")
                teamViewModel.getTeamList(activityViewModel.storeId.value!!)
            }.onFailure {error ->
                Log.e("QR_SCAN","아이디어가 실패하였습니다: ${error.localizedMessage}", error)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                Log.d(TAG, "onActivityResult: ")
                handleQRCodeScanResult(result.contents) // QR 코드 결과 처리
            } else {
                // QR 코드 스캔 취소 시
                Log.d("QR_SCAN", "QR 코드 스캔 취소됨")
            }
        } else {
            // 예외 처리 (IntentIntegrator가 반환한 결과가 null일 때)
            Log.e("QR_SCAN", "QR 코드 스캔 결과 처리 중 오류 발생")
        }
    }

    // 🔊 QR 코드 스캔 성공 시 "띠링" 효과음 재생 함수
    private fun playBeepSound() {
        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.beep_sound) // 🔹 beep_sound.mp3 파일을 사용
        if (mediaPlayer != null) {
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release() // 재생 완료 후 리소스 해제
            }
        } else {
            Log.e("playBeepSound", "미디어 플레이어 초기화 실패.")
        }
    }

    override fun onClick(teamId : Int, teamName:String) {
        activityViewModel.setTeamId(teamId)
        activityViewModel.setTeamName(teamName)
        mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.TEAM_FRAGMENT)
    }
}