package com.example.prepay.ui.GroupDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentGroupDetailsBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.RestaurantDetails.RestaurantDetailsFragment

class GroupDetailsFragment: BaseFragment<FragmentGroupDetailsBinding>(
    FragmentGroupDetailsBinding::bind,
    R.layout.fragment_group_details
){
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity= context as MainActivity

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        binding.restaurantDetail.setOnClickListener{
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.RESTAURANT_DETAILS_FRAGMENT)
        }
    }
}