package com.example.qrscanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.prepay.RetrofitUtil
import com.example.qrscanner.databinding.ActivityMainBinding
import com.example.qrscanner.response.PosReq
import com.example.qrscanner.response.orderDetail
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.qrbtn.setOnClickListener {
            startQRCodeScanner()
        }
    }

    // QR 코드 스캔을 시작하는 함수
    fun startQRCodeScanner() {
        val integrator = IntentIntegrator(this) // Fragment에서 사용할 경우 forSupportFragment
        integrator.setPrompt("Scan a QR code")
        integrator.setOrientationLocked(false) // 화면 회전 가능
        integrator.initiateScan() // QR 코드 스캔 시작
    }

    fun handleQRCodeScanResult(scanResult: String) {
        // QR 코드 데이터 처리
        Log.d("QR_SCAN", "QR 성공코드가 찍혔습니다: $scanResult")
        val parts = scanResult.split(":")
        // 주문 상세 정보 리스트 생성
        val orderDetails = listOf(
            orderDetail(detailPrice = 10000, product = "커피", quantity = 2),
            orderDetail(detailPrice = 5000, product = "샌드위치", quantity = 1)
        )
        // PosReq 객체 생성
        val posReq = PosReq(
            details = orderDetails,
            email = parts[1],
            qrUUID = parts[0],
            storeId = 3,
            teamId = parts[2].toInt()
        )
        lifecycleScope.launch {
            runCatching {
                RetrofitUtil.posService.posTransfer(posReq)
            }.onSuccess {
                Log.d("QR_SCAN","아이디어가 작동하였습니다")
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
}