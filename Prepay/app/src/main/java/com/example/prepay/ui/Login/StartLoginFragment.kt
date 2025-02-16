package com.example.prepay.ui.Login

import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentStartBinding
import com.example.prepay.ui.LoginActivity
import okhttp3.internal.http2.Header

class StartLoginFragment : BaseFragment<FragmentStartBinding>(
    FragmentStartBinding::bind,
    R.layout.fragment_start
) {
    private lateinit var loginActivity: LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 2초 후에 로그인 화면으로 전환
        delayRun({
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.LOGIN_FRAGMENT)
            // 필요 시 액티비티 종료 로직 추가 (예: finish())
        }, 1000)
    }

    private fun delayRun(action: () -> Unit, delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed(action, delayMillis)
    }
}