package com.example.prepay.ui.PublicReceiptList

import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentPublicReceiptListBinding
import com.example.prepay.ui.MainActivity

class PublicReceiptListFragment : BaseFragment<FragmentPublicReceiptListBinding>(
    FragmentPublicReceiptListBinding::bind,
    R.layout.fragment_public_receipt_list
){
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.hideBottomNav(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}