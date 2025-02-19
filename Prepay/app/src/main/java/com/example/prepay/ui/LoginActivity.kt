package com.example.prepay.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.prepay.ApplicationClass
import com.example.prepay.BaseActivity
import com.example.prepay.CommonUtils
import com.example.prepay.PermissionChecker
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.remote.FirebaseTokenService
import com.example.prepay.data.response.TokenReq
import com.example.prepay.databinding.ActivityLoginBinding
import com.example.prepay.response.LoginRequest
import com.example.prepay.ui.Login.FindPasswordFragment
import com.example.prepay.ui.Login.LoginFragment
import com.example.prepay.ui.Login.SignupFragment
import com.example.prepay.ui.Login.StartLoginFragment
import com.example.prepay.ui.Login.VerifyIdFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import kotlinx.coroutines.launch
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

        SharedPreferencesUtil.init(this)
        // SharedPreferences를 통한 자동 로그인 체크
        val (savedId, savedPw) = SharedPreferencesUtil.getUserCredentials()

//         저장된 아이디와 패스워드가 모두 있으면, 자동으로 로그인 API를 호출하여 자동 로그인 처리
        if (!savedId.isNullOrEmpty() && !savedPw.isNullOrEmpty()) {
            // 자동 로그인: 저장된 값으로 login() 함수를 호출
            Log.d(TAG, "${savedId}")
//            autoLogin(savedId, savedPw)
//            return
        }

        Log.d(TAG, "onCreate: START_LOGIN_FRAGMENT")
        changeFragmentLogin(CommonUtils.LoginFragmentName.LOGIN_FRAGMENT)
        checkPermission()
        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.scheme_kakao_app_key))
    }

    fun changeFragmentLogin(name: CommonUtils.LoginFragmentName, num: Int = -1) {
        val transaction = supportFragmentManager.beginTransaction()
        when (name) {

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

    private fun autoLogin(userId: String, userPw: String) {

        lifecycleScope.launch {
            runCatching {
                // 이제 uid와 upw는 null이 될 수 없는 String이므로 오류가 발생하지 않습니다.
                val loginRequest = LoginRequest(userId, userPw)
                RetrofitUtil.userService.login(loginRequest)
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val accessToken = response.headers()["access"]
                    if (accessToken != null) {
                        // Access Token 저장
                        val nickname = response.body()!!.nickname
                        SharedPreferencesUtil.setNickName(nickname)
                        SharedPreferencesUtil.saveAccessToken(accessToken)
                        Log.d(TAG, "Access Token 저장됨: $accessToken")
                    }
                    Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "로그인 실패: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.onFailure {
                Toast.makeText(this@LoginActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}