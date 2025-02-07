package com.example.prepay.ui.CreateGroup

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.PublicPrivateTeam
import com.example.prepay.databinding.FragmentCreatePrivateGroupBinding
import com.example.prepay.databinding.FragmentCreatePublicGroupBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.util.BootPayManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

private const val TAG = "CreatePublicGroupFragme"

class CreatePublicGroupFragment : BaseFragment<FragmentCreatePublicGroupBinding>(
    FragmentCreatePublicGroupBinding::bind,
    R.layout.fragment_create_public_group
) {
    private lateinit var mainActivity: MainActivity
    private val fragmentScope = lifecycleScope
    private var isCheckingRepeatUse = false
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageMultipart: MultipartBody.Part? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        imagePickerLauncher = getImagePickerLauncher()
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

        binding.imageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }

        binding.possible.setOnCheckedChangeListener { _, isChecked ->
            if (!isCheckingRepeatUse) {
                isCheckingRepeatUse = true
                binding.impossible.isChecked = !isChecked
                isCheckingRepeatUse = false
            }
        }

        binding.impossible.setOnCheckedChangeListener { _, isChecked ->
            if (!isCheckingRepeatUse) {
                isCheckingRepeatUse = true
                binding.possible.isChecked = !isChecked
                isCheckingRepeatUse = false
            }
        }

        binding.registerBtn.setOnClickListener { registerTeam() }
        binding.cancelBtn.setOnClickListener { mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT) }
        mainActivity.hideBottomNav(true)
    }

    private fun registerTeam() {
        val teamMakeRequest = PublicPrivateTeam(
            publicTeam = binding.publicCheckbox.isChecked,
            teamName = binding.groupNameText.text.toString(),
            dailyPriceLimit = binding.limitSettingText.text.toString().toInt(),
            countLimit = if (binding.possible.isChecked) 0 else 1,
            teamMessage = binding.textInputText.text.toString()
        )

        lifecycleScope.launch {
            try {
                binding.registerBtn.isEnabled = false

                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.teamService.makeTeam("1", teamMakeRequest, selectedImageMultipart)
                }

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "팀이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show()
//                    BootPayManager.startPayment(requireActivity(),binding.groupNameText.text.toString(), binding.bootpayAmount.toString())
                    mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
                } else {
                    Log.e(TAG, "팀 생성 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "네트워크 요청 실패: ${e.localizedMessage}", e)
            } finally {
                binding.registerBtn.isEnabled = true
            }
        }
    }

    private fun getFileNameFromUri(uri: Uri): String {
        var fileName = ""
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && it.moveToFirst()) {
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun getMultipartBodyFromUri(uri: Uri, partName: String): MultipartBody.Part {
        val file = File(requireContext().cacheDir, getFileNameFromUri(uri))
        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        return MultipartBody.Part.createFormData(partName, file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
    }

    private fun getImagePickerLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.imageBtn.text = getFileNameFromUri(uri)
                    selectedImageMultipart = getMultipartBodyFromUri(uri, "image")
                }
            }
        }
    }
    override fun onDestroyView() {
        fragmentScope.coroutineContext.cancelChildren()
        super.onDestroyView()
    }
}
