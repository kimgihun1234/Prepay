package com.example.prepay.ui.Login

import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentStartBinding
import com.example.prepay.ui.LoginActivity

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
        initEvent()
    }

    fun initEvent() {
        binding.login.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.LOGIN_FRAGMENT)
        }

        binding.createMember.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.SIGNIN_FRAGMENT)
        }
    }
}