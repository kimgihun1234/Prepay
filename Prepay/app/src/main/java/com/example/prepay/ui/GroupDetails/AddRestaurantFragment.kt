package com.example.prepay.ui.GroupDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.BootPayCharge
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.data.response.StoreIdReq
import com.example.prepay.databinding.FragmentAddRestaurantBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.util.BootPayManager
import kotlinx.coroutines.Dispatchers
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResults.visibility = View.GONE

        groupDetailsFragmentViewModel.getStoreId(StoreIdReq(0.0,0.0, 1))
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
            val totalPrice = binding.totalPrice.text.toString()

            if (selectedRestaurantName.isNotEmpty() && totalPrice.isNotEmpty()) {
                    BootPayManager.startPayment(requireActivity(), selectedRestaurantName, totalPrice) { teamId, storeId ->
                        Log.d(TAG, "teamId: ${teamId.toInt()}, ${storeId}, ${totalPrice.toInt()}")
                        lifecycleScope.launch {
                        try {
                            val chargeReceipt = BootPayCharge(teamId.toInt(), storeId, totalPrice.toInt(), "")
                            val response = withContext(Dispatchers.IO) {
                                RetrofitUtil.bootPayService.getBootPay("user1@gmail.com", chargeReceipt)
                            }

                            if (response.isSuccessful) {
                                Toast.makeText(requireContext(), "영수증이 성공적으로 들어갔습니다.", Toast.LENGTH_SHORT).show()
                                mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)
                            } else {
                                Log.e(TAG, "영수증 올리기 실패: ${response.code()}")
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
        searchAdapter = RestaurantSearchAdapter { selectedRestaurant ->
            binding.addRestaurantName.setQuery(selectedRestaurant.name, false)
            selectedRestaurantName = selectedRestaurant.name
            binding.searchResults.visibility = View.GONE
        }

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
