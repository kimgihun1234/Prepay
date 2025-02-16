package com.example.qrscanner.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.prepay.RetrofitUtil
import com.example.qrscanner.MainActivity
import com.example.qrscanner.MainActivityViewModel
import com.example.qrscanner.R
import com.example.qrscanner.base.BaseFragment
import com.example.qrscanner.databinding.FragmentLoginBinding
import com.example.qrscanner.response.PosReq
import com.example.qrscanner.response.orderDetail
import com.example.qrscanner.util.CommonUtils
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch

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