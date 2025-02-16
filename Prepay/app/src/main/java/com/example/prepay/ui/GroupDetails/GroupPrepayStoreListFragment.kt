package com.example.prepay.ui.GroupDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentGroupPaymentHistoryBinding
import com.example.prepay.databinding.FragmentGroupPrepayStoreListBinding
import com.example.prepay.ui.MainActivity

class GroupPrepayStoreListFragment: BaseFragment<FragmentGroupPrepayStoreListBinding>(
    FragmentGroupPrepayStoreListBinding::bind,
    R.layout.fragment_group_prepay_store_list
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