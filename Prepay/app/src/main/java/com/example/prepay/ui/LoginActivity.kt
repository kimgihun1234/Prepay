package com.example.prepay.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.example.prepay.ApplicationClass
import com.example.prepay.BaseActivity
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.remote.FirebaseTokenService
import com.example.prepay.data.response.TokenReq
import com.example.prepay.databinding.ActivityLoginBinding
import com.example.prepay.ui.Login.FindPasswordFragment
import com.example.prepay.ui.Login.LoginFragment
import com.example.prepay.ui.Login.SignupFragment
import com.example.prepay.ui.Login.StartLoginFragment
import com.example.prepay.ui.Login.VerifyIdFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import okhttp3.internal.http2.Header
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "MainActivity_싸피"
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val checker = PermissionChecker(this)
    private val runtimePermissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: START_LOGIN_FRAGMENT")
        changeFragmentLogin(CommonUtils.LoginFragmentName.START_LOGIN_FRAGMENT)
        checkPermission()
        SharedPreferencesUtil.init(this)
        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.scheme_kakao_app_key))
    }

    fun changeFragmentLogin(name: CommonUtils.LoginFragmentName, num: Int = -1) {
        val transaction = supportFragmentManager.beginTransaction()
        when (name) {
            CommonUtils.LoginFragmentName.START_LOGIN_FRAGMENT -> {
                transaction.replace(R.id.login_container, StartLoginFragment())
            }
            CommonUtils.LoginFragmentName.LOGIN_FRAGMENT -> {
                transaction.replace(R.id.login_container, LoginFragment())
            }
            CommonUtils.LoginFragmentName.SIGNIN_FRAGMENT -> {
                transaction.replace(R.id.login_container, SignupFragment())
            }
            CommonUtils.LoginFragmentName.FINDPASSWORD_FRAGMENT -> {
                transaction.add(R.id.login_container, FindPasswordFragment()).addToBackStack(null)
            }
            CommonUtils.LoginFragmentName.VERIFYID_FRAGMENT -> {
                transaction.add(R.id.login_container, VerifyIdFragment()).addToBackStack(null)
            }
        }
        transaction.commit()
    }

    private fun checkPermission() {
        /** permission check **/
        if (!checker.checkPermission(this, runtimePermissions)) {
            checker.setOnGrantedListener{ //퍼미션 획득 성공일때
            }
            checker.requestPermissionLauncher.launch(runtimePermissions) // 권한없으면 창 띄움
        } else { //이미 전체 권한이 있는 경우
        }
        /** permission check **/
    }

}