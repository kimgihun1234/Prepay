package com.example.prepay.ui.LookGroup

import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentLookGroupBinding
import com.example.prepay.ui.LoginActivity

class LookGroupFragment: BaseFragment<FragmentLookGroupBinding>(
    FragmentLookGroupBinding::bind,
    R.layout.fragment_look_group
){
    private lateinit var loginActivity: LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}