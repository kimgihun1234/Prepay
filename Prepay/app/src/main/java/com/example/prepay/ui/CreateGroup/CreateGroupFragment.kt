package com.example.prepay.ui.CreateGroup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.databinding.FragmentCreateGroupBinding
import com.example.prepay.ui.MainActivity


class CreateGroupFragment : BaseFragment<FragmentCreateGroupBinding>(
    FragmentCreateGroupBinding::bind,
    R.layout.fragment_create_group
){
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.changeFragmentGroup(CommonUtils.GroupFragmentName.CREATE_PRIVATE_GROUP_FRAGMENT)
        binding.publicCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.privateCheckbox.isChecked = false
                mainActivity.changeFragmentGroup(CommonUtils.GroupFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT)
            }
        }
        binding.privateCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.publicCheckbox.isChecked = false
                mainActivity.changeFragmentGroup(CommonUtils.GroupFragmentName.CREATE_PRIVATE_GROUP_FRAGMENT)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }
    }
}