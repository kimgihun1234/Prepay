package com.example.qrscanner.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.qrscanner.MainActivity
import com.example.qrscanner.MainActivityViewModel
import com.example.qrscanner.R
import com.example.qrscanner.base.BaseFragment
import com.example.qrscanner.databinding.FragmentLoginBinding
import com.example.qrscanner.util.CommonUtils

private const val TAG = "LoginFragment"
class LoginFragment : BaseFragment<FragmentLoginBinding> (
    FragmentLoginBinding::bind,
    R.layout.fragment_login
) {
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        binding.qrbtn2.setOnClickListener {
            val num = binding.storeInput2.text
            Log.d(TAG, "initEvent: $num")
            activityViewModel.setStoreId(num.toString().toInt())
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.STORE_TEAM_FRAGMENT)
        }
    }


}