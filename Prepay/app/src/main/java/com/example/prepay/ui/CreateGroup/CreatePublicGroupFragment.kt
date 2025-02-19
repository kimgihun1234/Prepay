package com.example.prepay.ui.CreateGroup

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.response.BootPayChargeReq
import com.example.prepay.data.model.dto.PublicPrivateTeam
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.data.response.TeamIdRes
import com.example.prepay.data.response.TeamStoreReq
import com.example.prepay.databinding.FragmentCreatePublicGroupBinding
import com.example.prepay.ui.GroupDetails.RestaurantSearchAdapter
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.example.prepay.util.BootPayManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
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

    private lateinit var receiptIdText: String
    private lateinit var priceText: String

    private lateinit var editTexts: List<EditText>
    private lateinit var searchAdapter: RestaurantSearchAdapter
    private var selectedRestaurantName: String = ""

    private val createGroupViewModel : CreateGroupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        imagePickerLauncher = getImagePickerLauncher()

        editTexts = listOf(
            binding.groupNameText,
            binding.bootpayAmount,
            binding.limitSettingText,
            binding.textInputText
        )
        binding.searchResults.visibility = View.GONE


        createGroupViewModel.getAllStore(SharedPreferencesUtil.getAccessToken()!!)
        initRecyclerView()
        setOnQueryTextListener()
        initEvent()
        observeStoresListInfo()
        initFocusChangeListener()
    }
    private fun observeStoresListInfo() {
        createGroupViewModel.storeListInfo.observe(viewLifecycleOwner) { stores ->
            val queryText = binding.searchRestaurant.query.toString()

            if (stores.isNotEmpty() && queryText.isNotEmpty()) {
                val restaurantList = stores.map { Restaurant(it.storeId, it.storeName) }
                searchAdapter.submitList(restaurantList)
                binding.searchResults.visibility = View.VISIBLE
            } else {
                searchAdapter.submitList(emptyList())
                binding.searchResults.visibility = View.GONE
            }
        }
    }

    private fun initEvent() {
        binding.image.setOnClickListener {
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

        binding.registerBtn.setOnClickListener {
            registerTeam()
        }

        binding.cancelBtn.setOnClickListener { mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT) }
        mainActivity.hideBottomNav(true)
    }


    private fun registerTeam() {
        if (!validateInputs()) {
            return
        }

        val teamMakeRequest = PublicPrivateTeam(
            publicTeam = true,
            teamName = binding.groupNameText.text.toString(),
            dailyPriceLimit = binding.limitSettingText.text.toString().toInt(),
            countLimit = if (binding.possible.isChecked) 0 else 1,
            teamMessage = binding.textInputText.text.toString()
        )

        BootPayManager.startPayment(requireActivity(), selectedRestaurantName, binding.bootpayAmount.text.toString()) { receiptId, price ->

            lifecycleScope.launch {
                try {
                    binding.registerBtn.isEnabled = false
                    val response = withContext(Dispatchers.IO) {
                        RetrofitUtil.teamService.makeTeam(
                            SharedPreferencesUtil.getAccessToken()!!,
                            teamMakeRequest,
                            selectedImageMultipart
                        )
                    }

                    if (response.isSuccessful) {
                        receiptIdText = receiptId
                        priceText = price.toString()
                        Toast.makeText(requireContext(), "팀이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show()
                        val teamIdRes : TeamIdRes? = response.body()
                        teamIdRes?.let { createGroupViewModel.updateTeamId(it) }
                        Log.d(TAG, "teamId: ${teamIdRes?.teamId}")
                        registerStore()
                        registerReceipt()
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
    }

    private fun registerStore() {
        lifecycleScope.launch {
            try {
                binding.registerBtn.isEnabled = false
                // teamId와 storeId를 안전하게 가져오기
                val teamId = createGroupViewModel.teamId.value?.teamId
                val storeId = createGroupViewModel.storeId.value

                if (teamId == null || storeId == null) {
                    return@launch
                }
                Log.d(TAG, "storeId: $storeId")
                Log.d(TAG, "teamId: $teamId")
                val request = TeamStoreReq(storeId, teamId)
                Log.d(TAG, "selectedImageMultipart: $selectedImageMultipart")
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.teamService.createStore(SharedPreferencesUtil.getAccessToken()!!, request)
                }

                Log.d(TAG, "response: $response")

                if (response.isSuccessful) {

                } else {
                    Log.e(TAG, "스토어 연결 실패: ${response?.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "네트워크 요청 실패: ${e.localizedMessage}", e)
            } finally {
                binding.registerBtn.isEnabled = true
            }
        }
    }

    private fun registerReceipt() {
        lifecycleScope.launch {
            try {
                val temaIdRes : LiveData<TeamIdRes> = createGroupViewModel.teamId
                val storeId = createGroupViewModel.storeId.value
                val chargeReceipt =
                    storeId?.let { store ->
                        temaIdRes.value?.teamId?.let { team ->
                            BootPayChargeReq(
                                team, store, priceText.toInt(), receiptIdText)
                        }
                    }

                val response = withContext(Dispatchers.IO) {
                    delay(1000)
                    chargeReceipt?.let {
                        RetrofitUtil.bootPayService.getBootPay(SharedPreferencesUtil.getAccessToken()!!,
                            it
                        )
                    }
                }
                if (response?.isSuccessful == true) {
                    mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)

                } else {
                    Log.e(TAG, "영수증 올리기 실패: ${response}")
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
                    binding.image.setImageURI(uri)
                    selectedImageMultipart = getMultipartBodyFromUri(uri, "image")
                }
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

    private fun validateInputs(): Boolean {
        if (binding.groupNameText.text.isNullOrBlank()) {
            showToast("그룹명을 입력해주세요.")
            return false
        }
        if (selectedImageMultipart == null) {
            showToast("이미지를 선택해주세요.")
            return false
        }
        if (selectedRestaurantName.isEmpty()) {
            showToast("식당을 검색해주세요.")
            return false
        }
        if (binding.bootpayAmount.text.isNullOrBlank() || binding.bootpayAmount.text.toString().toIntOrNull() == null || binding.bootpayAmount.text.toString().toInt() < 1000) {
            showToast("올바른 결제 금액을 입력해주세요.")
            return false
        }
        if (binding.limitSettingText.text.isNullOrBlank() || binding.limitSettingText.text.toString().toIntOrNull() == null) {
            showToast("일일 제한 금액을 입력해주세요.")
            return false
        }
        if (!binding.possible.isChecked && !binding.impossible.isChecked) {
            showToast("반복 사용을 체크해주세요.")
            return false
        }
        if (binding.textInputText.text.isNullOrBlank()) {
            showToast("팀 메시지를 입력해주세요.")
            return false
        }


        return true
    }

    private fun initRecyclerView() {
        searchAdapter = RestaurantSearchAdapter ({ selectedRestaurant ->
            binding.searchRestaurant.setQuery(selectedRestaurant.name, false)
            selectedRestaurantName = selectedRestaurant.name
            binding.searchResults.visibility = View.GONE
        }, createGroupViewModel)

        binding.searchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
    }

    private fun setOnQueryTextListener() {
        binding.searchRestaurant.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    binding.searchResults.visibility = View.GONE
                } else {
                    filterSearchResults(newText)
                    binding.searchResults.visibility = View.VISIBLE
                }
                return true
            }
        })
    }

    private fun filterSearchResults(query: String) {
        val storeList = createGroupViewModel.storeListInfo.value
        if (storeList == null) {
            Log.e(TAG, "storesListInfo is null, cannot filter results.")
            return
        }

        val filteredList = getSearchResults(query)
        Log.d(TAG, "filterSearchResults: $filteredList")
        searchAdapter.submitList(filteredList)
    }

    private fun getSearchResults(query: String): List<Restaurant> {
        // 여기에 식당 검색 로직 추가

        val storeList = createGroupViewModel.storeListInfo.value
        Log.d(TAG, "getSearchResults: ${createGroupViewModel.storeListInfo.value}")
        return storeList?.map { store ->
            Restaurant(store.storeId, store.storeName)
        }?.filter { it.name.contains(query, ignoreCase = true) } ?: emptyList()

    }
}
