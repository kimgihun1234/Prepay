package com.example.prepay.ui.GroupSearch

import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentGroupSearchBinding
import com.example.prepay.ui.MainActivity

class GroupSearchFragment: BaseFragment<FragmentGroupSearchBinding>(
    FragmentGroupSearchBinding::bind,
    R.layout.fragment_group_search
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