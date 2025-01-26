package com.example.prepay.ui.CreateGroup

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.data.model.dto.PublicGroup
import com.example.prepay.databinding.FragmentCreatePublicGroupBinding
import com.example.prepay.ui.MainActivity

private const val TAG = "CreatePublicGroupFragme"
class CreatePublicGroupFragment: BaseFragment<FragmentCreatePublicGroupBinding>(
    FragmentCreatePublicGroupBinding::bind,
    R.layout.fragment_create_public_group
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
        binding.publicCheckbox.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT)
        }
        binding.privateCheckbox.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_PRIVATE_GROUP_FRAGMENT)
        }
        binding.registerBtn.setOnClickListener {
            val team_name = binding.groupNameText.getText().toString()
            val image_url = binding.imageBtn.urls
            val daily_price_limit = binding.limitSettingText.getText().toString()
            val repeat_use = binding.possible.getText().toString()
            val context_text = binding.textInputText.getText().toString()

            // POST로 넘기기
            Log.d(TAG, "team_name: $team_name")
            Log.d(TAG, "daily_price_limit: $daily_price_limit")
            Log.d(TAG, "repeat_use: $repeat_use")
            Log.d(TAG, "image_url: $image_url")
            Log.d(TAG, "context_text: $context_text")

            PublicGroup(team_name, daily_price_limit.toInt(), repeat_use, image_url.toString(), context_text)
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }
        binding.cancelBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }
        //내비게이션 바 없어지게 mainActivity에 함수가 있습니다.
        mainActivity.hideBottomNav(true)
    }
}