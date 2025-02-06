package com.example.prepay.ui.CreateGroup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.PublicPrivateTeam
import com.example.prepay.data.model.dto.ImageRequest
import com.example.prepay.databinding.FragmentCreatePrivateGroupBinding
import com.example.prepay.ui.MainActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancelChildren

private const val TAG = "CreatePrivateGroupFragm"

class CreatePrivateGroupFragment: BaseFragment<FragmentCreatePrivateGroupBinding>(
    FragmentCreatePrivateGroupBinding::bind,
    R.layout.fragment_create_private_group
) {
    private lateinit var mainActivity: MainActivity
    private val fragmentScope = lifecycleScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
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

        binding.privateRegisterBtn.setOnClickListener {
            if (validateInputs()) {
                val privateTeam = !binding.privateCheckbox.isChecked
                val groupName = binding.groupNameText.text.toString()
                val limitAmount = binding.limitSettingText.text.toString()

                val makePrivateTeam = PublicPrivateTeam(
                    privateTeam,
                    groupName,
                    limitAmount.toInt()
                )
                makeTeam(makePrivateTeam)
            }
        }

        binding.cancelBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }

        // 내비게이션 바 없어지게
        mainActivity.hideBottomNav(true)
    }

    private fun validateInputs(): Boolean {
        val groupName = binding.groupNameText.text.toString()
        val limitAmount = binding.limitSettingText.text.toString()

        if (groupName.isEmpty()) {
            Toast.makeText(requireContext(), "그룹 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (limitAmount.isEmpty()) {
            Toast.makeText(requireContext(), "제한 금액을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        try {
            limitAmount.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "올바른 금액을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun makeTeam(teamMakeRequest: PublicPrivateTeam) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                binding.privateRegisterBtn.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
                val header: String   = "1"
                RetrofitUtil.teamService.makeTeam(header, teamMakeRequest, null)

                Log.d(TAG, "makeTeam: 성공")

                if (isAdded) {  // Fragment가 아직 활성 상태인지 확인
                    Toast.makeText(requireContext(), "팀이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show()
                    mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
                }
            } catch (e: Exception) {
                Log.e(TAG, "teamMakeRequest 실패, 예외 메시지: ${e.message}", e)
                if (isAdded) {  // Fragment가 아직 활성 상태인지 확인
                    Toast.makeText(requireContext(), "팀 생성 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                if (isAdded && view != null) {
                    binding.privateRegisterBtn.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        fragmentScope.coroutineContext.cancelChildren()
        super.onDestroyView()
    }
}