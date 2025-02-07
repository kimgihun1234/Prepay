package com.example.prepay.ui.GroupSearchDetails

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.DialogQrDiningTogetherBinding
import com.example.prepay.databinding.FragmentPublicGroupDetailsBinding
import com.example.prepay.ui.MainActivity

private const val TAG = "PublicGroupDetailsFragment"
class AddPublicGroupDetailsFragment: BaseFragment<FragmentPublicGroupDetailsBinding>(
    FragmentPublicGroupDetailsBinding::bind,
    R.layout.fragment_public_group_details
) {
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        val groupDetail = ViewModelProvider(requireActivity()).get(GroupSearchtDetailsViewModel::class.java)

        Log.d(com.example.prepay.ui.GroupSearchDetails.TAG, "initEvent: ${groupDetail.groupDetailsData.value}")
        binding.publicDetailTeamName.text = groupDetail.groupDetailsData.value

        binding.publicDetailQrBtn.setOnClickListener {
            val dialogBinding = DialogQrDiningTogetherBinding.inflate(layoutInflater)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogBinding.root)
                .create()
            dialog.show()

            // 다이얼로그 외부를 터치시 닫음
            dialog.setCanceledOnTouchOutside(true)
        }
    }

}