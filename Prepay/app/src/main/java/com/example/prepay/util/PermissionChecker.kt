package com.example.prepay

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

fun interface OnGrantedListener {
    fun onGranted()
}


private const val TAG = "CheckPermission_싸피"
class PermissionChecker(activityOrFragment: Any) {
    private lateinit var context:Context

    private lateinit var permitted : OnGrantedListener
    fun setOnGrantedListener(listener: OnGrantedListener){
        permitted = listener
    }

    // 권한 체크
    fun checkPermission(context: Context, permissions: Array<String>): Boolean {
        this.context = context
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission( context, permission ) != PackageManager.PERMISSION_GRANTED ) {
                return false
            }
        }

        return true
    }

    // 권한 호출한 이후 결과받아서 처리할 Launcher (startPermissionRequestResult )
    val requestPermissionLauncher: ActivityResultLauncher<Array<String>> = when (activityOrFragment) {
        is AppCompatActivity -> {
            activityOrFragment.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()){
                resultChecking(it)
            }
        }

        is Fragment -> {
            activityOrFragment.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()){
                resultChecking(it)
            }
        }

        else -> {
            throw RuntimeException("Activity혹은 Fragment에서 권한설정이 가능합니다.")
        }
    }


    private fun resultChecking(result: Map<String, Boolean>){
        Log.d(TAG, "requestPermissionLauncher: 건수 : ${result.size}")

        if(result.values.contains(false)){ //false가 있는 경우라면..
            Toast.makeText(context, "권한이 부족합니다.", Toast.LENGTH_SHORT).show()
            moveToSettings()
        }else{
            Toast.makeText(context, "모든 권한이 허가되었습니다.", Toast.LENGTH_SHORT).show()
            permitted.onGranted()
        }
    }

    //사용자가 권한을 허용하지 않았을때, 설정창으로 이동
    private fun moveToSettings() {
        val alertDialog = AlertDialog.Builder( context )
        alertDialog.setTitle("권한이 필요합니다.")
        alertDialog.setMessage("설정으로 이동합니다.")
        alertDialog.setPositiveButton("확인") { dialogInterface, i -> // 안드로이드 버전에 따라 다를 수 있음.
            val intent =
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + context.packageName))
            context.startActivity(intent)
            dialogInterface.cancel()
        }
        alertDialog.setNegativeButton("취소") { dialogInterface, i -> dialogInterface.cancel() }
        alertDialog.show()
    }
}



