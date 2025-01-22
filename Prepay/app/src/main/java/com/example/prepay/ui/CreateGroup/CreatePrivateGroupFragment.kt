package com.example.prepay.ui.CreateGroup

import android.os.Bundle
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentCreatePrivateGroupBinding
import com.example.prepay.ui.MainActivity


class CreatePrivateGroupFragment: BaseFragment<FragmentCreatePrivateGroupBinding>(
    FragmentCreatePrivateGroupBinding::bind,
    R.layout.fragment_create_private_group
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
        binding.publicBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT)
        }
        binding.registerBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }
        binding.cancelBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }
        //내비게이션 바 없어지게
        mainActivity.hideBottomNav(true)
    }
}