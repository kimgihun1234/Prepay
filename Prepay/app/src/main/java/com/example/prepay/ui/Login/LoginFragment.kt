package com.example.prepay.ui.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentLoginBinding
import com.example.prepay.ui.LoginActivity
import com.example.prepay.ui.MainActivity


class LoginFragment: BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::bind,
    R.layout.fragment_login
){
    private lateinit var loginActivity: LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    fun initEvent(){
        binding.JoinBtn.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.SIGNIN_FRAGMENT)
        }
        binding.LoginBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        binding.findIdBtn.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.FINDID_FRAGMENT)
        }
        binding.findPasswordBtn.setOnClickListener {
            loginActivity.changeFragmentLogin(CommonUtils.LoginFragmentName.FINDPASSWORD_FRAGMENT)
        }
    }
}