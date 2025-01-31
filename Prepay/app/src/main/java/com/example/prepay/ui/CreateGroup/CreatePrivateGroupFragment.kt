package com.example.prepay.ui.CreateGroup

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.data.model.dto.PublicPrivateTeam
import com.example.prepay.databinding.FragmentCreatePrivateGroupBinding
import com.example.prepay.ui.MainActivity

private const val TAG = "CreatePrivateGroupFragm"
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
        binding.publicCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.privateCheckbox.isChecked = false
                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT)
            }
        }
        binding.privateCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.publicCheckbox.isChecked = false
                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.CREATE_PRIVATE_GROUP_FRAGMENT)
            }
        }
        binding.registerBtn.setOnClickListener {
            val privateTeam = if(binding.privateCheckbox.isChecked) 1 else 0
            val groupName = binding.groupNameText.text.toString()
            val limitAmount = binding.limitSettingText.text.toString()

            // POST로 데려가서 API에 저장할 것이다.
            PublicPrivateTeam(privateTeam, groupName, limitAmount.toInt())
            Log.d(TAG, "privateTeam: $privateTeam")
            Log.d(TAG, "groupName: $groupName")
            Log.d(TAG, "limitAmount: $limitAmount")

            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }
        binding.cancelBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }
        //내비게이션 바 없어지게
        mainActivity.hideBottomNav(true)
    }
}