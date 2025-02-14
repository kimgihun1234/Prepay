package com.example.prepay.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "MainActivity_싸피"
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val checker = PermissionChecker(this)
    private val runtimePermissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                init()
            }

            checker.requestPermissionLauncher.launch(runtimePermissions) // 권한없으면 창 띄움
        } else { //이미 전체 권한이 있는 경우
            init()
        }
        /** permission check **/
    }

    private fun init(){
        initFCM()
        createNotificationChannel(channel_id, "ssafy")
    }

    private fun initFCM(){
        // FCM 토큰 수신
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "FCM 토큰 얻기에 실패하였습니다.", task.exception)
                return@OnCompleteListener
            }
            // token log 남기기
            Log.d(TAG, "token: ${task.result?:"task.result is null"}")
            if(task.result != null){
                val tr = TokenReq(task.result!!)
                uploadToken(tr)
            }
        })
    }

    // Notification 수신을 위한 체널 추가
    private fun createNotificationChannel(id: String, name: String){
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(id, name, importance))
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        const val channel_id = "ssafy_channel"

        // ratrofit  수업 후 network 에 업로드 할 수 있도록 구성
        fun uploadToken(token: TokenReq) {
            // 새로운 토큰 수신 시 서버로 전송
            val storeService = ApplicationClass.retrofit.create(FirebaseTokenService::class.java)
            storeService.uploadToken("user1@gmail.com",token).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.d(TAG, "onResponse: $res")
                    } else {
                        Log.d(TAG, "로그인 onResponse: Error Code ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d(TAG, t.message ?: "토큰 정보 등록 중 통신오류")
                }
            })
        }
    }
}