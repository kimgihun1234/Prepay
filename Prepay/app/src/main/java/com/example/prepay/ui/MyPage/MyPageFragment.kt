package com.example.prepay.ui.MyPage

import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentMyPageBinding
import com.example.prepay.ui.MainActivity

class MyPageFragment: BaseFragment<FragmentMyPageBinding>(
    FragmentMyPageBinding::bind,
    R.layout.fragment_my_page
){
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    fun initEvent(){
        binding.cardBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.GROUP_DETAILS_FRAGMENT)
        }
        binding.createGroupBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT)
        }
        //내비게이션 바 생기게
        mainActivity.hideBottomNav(false)
    }

}