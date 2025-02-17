package com.example.prepay.ui.GroupDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.response.BootPayChargeReq
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.data.response.TeamStoreReq
import com.example.prepay.databinding.FragmentAddRestaurantBinding
import com.example.prepay.ui.CreateGroup.CreateGroupViewModel
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.example.prepay.util.BootPayManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "AddRestaurantFragment"
class AddRestaurantFragment : BaseFragment<FragmentAddRestaurantBinding>(
    FragmentAddRestaurantBinding::bind,
    R.layout.fragment_add_restaurant
) {
    private lateinit var mainActivity: MainActivity
    private lateinit var searchAdapter: RestaurantSearchAdapter
    private var selectedRestaurantName: String = ""

    private val groupDetailsFragmentViewModel: GroupDetailsFragmentViewModel by viewModels()
    private val createGroupViewModel : CreateGroupViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResults.visibility = View.GONE

        groupDetailsFragmentViewModel.getStoreId(SharedPreferencesUtil.getAccessToken()!!, activityViewModel.teamId.value!!.toString().toLong())
        Log.d(TAG, "AddRestaurantFragment: ${activityViewModel.teamId.value!!.toString().toLong()}")
        initRecyclerView()
        setOnQueryTextListener()
        initEvent()
        observeStoresListInfo()
    }

    private fun observeStoresListInfo() {
        groupDetailsFragmentViewModel.storesListInfo.observe(viewLifecycleOwner) { stores ->
            val queryText = binding.addRestaurantName.query.toString()

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
        val restaurant = groupDetailsFragmentViewModel
        binding.addRestaurantName.setQuery(restaurant.storesListInfo.value?.joinToString(", ") { it.storeName }, false)

        binding.confirm.setOnClickListener {
            val storeId = createGroupViewModel.storeId.value
            val totalPrice = binding.totalPrice.text.toString()
            val teamId : Long? = activityViewModel.teamId.value
            Log.d(TAG, "selectedRestaurantName: $selectedRestaurantName")
            Log.d(TAG, "totalPrice: $totalPrice")
            if (selectedRestaurantName.isNotEmpty() && totalPrice.isNotEmpty()) {
                    BootPayManager.startPayment(requireActivity(), selectedRestaurantName, totalPrice) { receiptId, price ->

                        Log.d(TAG, "storeId: $storeId")
                        Log.d(TAG, "teamId: $teamId, price: ${price}")
                        Log.d(TAG, "receiptId: $receiptId")
                        registerStore(storeId,teamId)
                        lifecycleScope.launch {
                            try {
                                val chargeReceipt = teamId?.let { teamId -> storeId?.let { storeId ->
                                    BootPayChargeReq(
                                        teamId.toInt(),storeId, totalPrice.toInt(), receiptId)
                                } }
                                val response = withContext(Dispatchers.IO) {
                                    delay(1000)
                                    chargeReceipt?.let { it ->
                                        RetrofitUtil.bootPayService.getBootPay(SharedPreferencesUtil.getAccessToken()!!,
                                            it
                                        )
                                    }
                            }

                            if (response?.isSuccessful == true) {
                                Toast.makeText(requireContext(), "영수증이 성공적으로 들어갔습니다.", Toast.LENGTH_SHORT).show()
                                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
                            } else {
                                Log.e(TAG, "영수증 올리기 실패: ${response}")
                            }
                            } catch (e: Exception) {
                                Log.e(TAG, "네트워크 요청 실패: ${e.localizedMessage}", e)
                            }
                        }
                    }
            } else {
                showError("식당 이름과 결제금액을 모두 입력해주세요.")
            }
        }
    }

    private fun registerStore(storeId: Int?, teamId: Long?) {
        lifecycleScope.launch {
            try {
                Log.d("registerStore", "storeId: $storeId")
                Log.d("registerStore", "teamId: $teamId")

                if (teamId == null || storeId == null) {
                    Toast.makeText(requireContext(), "팀 ID 또는 스토어 ID가 없습니다.", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                val request = TeamStoreReq(storeId, teamId.toInt())
                val response = withContext(Dispatchers.IO) {
                    RetrofitUtil.teamService.createStore(SharedPreferencesUtil.getAccessToken()!!, request)
                }

                Log.d(TAG, "response: $response")

                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "스토어가 성공적으로 연결되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "스토어 연결 실패: ${response?.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.e(TAG, "네트워크 요청 실패: ${e.localizedMessage}", e)
            } finally {

            }
        }
    }


    private fun setOnQueryTextListener() {
        binding.addRestaurantName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

    private fun showError(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }

    private fun initRecyclerView() {
        searchAdapter = RestaurantSearchAdapter ({ selectedRestaurant ->
            binding.addRestaurantName.setQuery(selectedRestaurant.name, false)
            selectedRestaurantName = selectedRestaurant.name
            binding.searchResults.visibility = View.GONE
        }, createGroupViewModel)

        binding.searchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
    }

    private fun filterSearchResults(query: String) {
        val storeList = groupDetailsFragmentViewModel.storesListInfo.value
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
        val storeList = groupDetailsFragmentViewModel.storesListInfo.value
        Log.d(TAG, "getSearchResults: ${groupDetailsFragmentViewModel.storesListInfo.value}")
        return storeList?.map { store ->
            Restaurant(store.storeId, store.storeName)
        }?.filter { it.name.contains(query, ignoreCase = true) } ?: emptyList()

    }
}
