package com.example.prepay.ui.CreateGroup

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
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
        binding.privateCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.checked_color))
        mainActivity.changeFragmentGroup(CommonUtils.GroupFragmentName.CREATE_PRIVATE_GROUP_FRAGMENT)
        binding.publicCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.privateCheckbox.isChecked = false
                binding.publicCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.checked_color))
                binding.privateCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.unchecked_color))
                mainActivity.changeFragmentGroup(CommonUtils.GroupFragmentName.CREATE_PUBLIC_GROUP_FRAGMENT)

            }
        }
        binding.privateCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.publicCheckbox.isChecked = false
                binding.privateCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.checked_color))
                binding.publicCheckbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.unchecked_color))
                mainActivity.changeFragmentGroup(CommonUtils.GroupFragmentName.CREATE_PRIVATE_GROUP_FRAGMENT)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }
    }
}