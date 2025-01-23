package com.example.prepay.ui

import android.os.Bundle
import com.example.prepay.BaseActivity
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.ActivityLoginBinding
import com.example.prepay.ui.Login.FindIdFragment
import com.example.prepay.ui.Login.FindPasswordFragment
import com.example.prepay.ui.Login.LoginFragment
import com.example.prepay.ui.Login.SignInFragment
import com.example.prepay.ui.Login.StartLoginFragment
import com.example.prepay.ui.Login.VerifyIdFragment


private const val TAG = "MainActivity_싸피"
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeFragmentLogin(CommonUtils.LoginFragmentName.START_LOGIN_FRAGMENT)
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
                transaction.replace(R.id.login_container, SignInFragment())
            }
            CommonUtils.LoginFragmentName.FINDID_FRAGMENT -> {
                transaction.add(R.id.login_container, FindIdFragment()).addToBackStack(null)
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

    companion object{

    }
}