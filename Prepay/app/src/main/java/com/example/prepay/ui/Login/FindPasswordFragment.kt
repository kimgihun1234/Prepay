package com.example.prepay.ui.Login

import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentFindPasswordBinding
import com.example.prepay.ui.LoginActivity

class FindPasswordFragment: BaseFragment<FragmentFindPasswordBinding>(
    FragmentFindPasswordBinding::bind,
    R.layout.fragment_find_password
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

    }
}