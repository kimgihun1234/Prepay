package com.example.prepay.ui

import android.content.Intent
import android.os.Bundle
import com.example.prepay.BaseActivity
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.ActivityLoginBinding
import com.example.prepay.ui.Login.LoginFragment
import com.example.prepay.ui.Login.SignInFragment


private const val TAG = "MainActivity_싸피"
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeFragmentLogin(CommonUtils.LoginFragmentName.LOGIN_FRAGMENT)
    }

    fun changeFragmentLogin(name: CommonUtils.LoginFragmentName, num: Int = -1) {
        val transaction = supportFragmentManager.beginTransaction()
        when (name) {
            CommonUtils.LoginFragmentName.LOGIN_FRAGMENT -> {
                transaction.replace(R.id.LoginContainer, LoginFragment())
            }
            CommonUtils.LoginFragmentName.SIGNIN_FRAGMENT -> {
                transaction.replace(R.id.LoginContainer, SignInFragment())
            }
        }
        transaction.commit()
    }

    companion object{

    }
}