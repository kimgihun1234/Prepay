package com.example.prepay.ui.GroupDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentGroupPaymentHistoryBinding
import com.example.prepay.databinding.FragmentLookGroupBinding
import com.example.prepay.ui.LoginActivity
import com.example.prepay.ui.MainActivity

class GroupPaymentHistoryFragment: BaseFragment<FragmentGroupPaymentHistoryBinding>(
    FragmentGroupPaymentHistoryBinding::bind,
    R.layout.fragment_group_payment_history
){
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}