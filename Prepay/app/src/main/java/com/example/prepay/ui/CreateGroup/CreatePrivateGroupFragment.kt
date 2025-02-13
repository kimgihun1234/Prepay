package com.example.prepay.ui.CreateGroup

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.PublicPrivateTeam
import com.example.prepay.databinding.FragmentCreatePrivateGroupBinding
import com.example.prepay.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext

private const val TAG = "CreatePrivateGroupFragm"

class CreatePrivateGroupFragment: BaseFragment<FragmentCreatePrivateGroupBinding>(
    FragmentCreatePrivateGroupBinding::bind,
    R.layout.fragment_create_private_group
) {
    private lateinit var mainActivity: MainActivity
    private val fragmentScope = lifecycleScope
    private lateinit var editTexts: List<EditText>
    private lateinit var colorAdapter : ColorAdapter
    private val createGroupViewModel : CreateGroupViewModel by viewModels()
    private lateinit var colorCard : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTexts = listOf(
            binding.groupNameText,
            binding.limitSettingText
        )

        initEvent()
        initAdapter()
        initFocusChangeListener()
    }



    private fun initEvent() {
        binding.privateRegisterBtn.setOnClickListener {
            if (validateInputs()) {
                val groupName = binding.groupNameText.text.toString()
                val limitAmount = binding.limitSettingText.text.toString()
                val cardType = colorCard?:"FFFFFF"
                val makePrivateTeam = PublicPrivateTeam(
                    false,
                    groupName,
                    limitAmount.toInt(),
                    cardType
                )
                makeTeam(makePrivateTeam)
            }
        }
        binding.colorButton.setOnClickListener {
            binding.categoryPaletteRv.isVisible = !binding.categoryPaletteRv.isVisible
        }

        binding.cardView.setOnClickListener{

        }

        binding.cancelBtn.setOnClickListener {
            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
        }

        // 내비게이션 바 없어지게
        mainActivity.hideBottomNav(true)
    }
    private fun initAdapter() {
        val colorAdapter = ColorAdapter(arrayListOf()) { color ->
            // onClick 처리
            colorCard = color
            val col = Color.parseColor(color)
            binding.cardView.setBackgroundColor(col)
        }
        val colorArray = resources.getStringArray(R.array.categoryColorArr)
        binding.categoryPaletteRv.adapter = colorAdapter
        Log.d(TAG, "initAdapter: ${colorArray.size}")
        Log.d(TAG, "initAdapter: ${colorArray.get(0)}")

        createGroupViewModel.colorList.observe(viewLifecycleOwner) { colorList ->
            colorAdapter.colorList = colorList
            colorAdapter.notifyDataSetChanged()
        }

        createGroupViewModel.setColorList(requireContext(), colorArray)

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
        lifecycleScope.launch {
            try {
                binding.privateRegisterBtn.isEnabled = false
                val header: String = "1"

                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.teamService.makeTeam(header, teamMakeRequest, null)
                }
                Log.d(TAG, "makeTeam: 성공")

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "팀이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show()
                    mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
                } else {
                    Log.e(TAG, "팀 생성 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "teamMakeRequest 실패, 예외 메시지: ${e.message}", e)
                if (isAdded) {
                    Toast.makeText(requireContext(), "팀 생성 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                binding.privateRegisterBtn.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        fragmentScope.coroutineContext.cancelChildren()
        super.onDestroyView()
    }

    // 효과
    private fun initFocusChangeListener() {
        editTexts.forEach {
            it.setOnFocusChangeListener { _, isFocus ->
                if (isFocus) {
                    it.setBackgroundResource(R.drawable.focus_shape_alll_round)
                } else {
                    it.setBackgroundResource(R.drawable.shape_all_round)
                }
            }
        }
    }
}