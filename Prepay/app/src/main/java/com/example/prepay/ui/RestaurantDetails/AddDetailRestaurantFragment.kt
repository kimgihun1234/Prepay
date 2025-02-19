package com.example.prepay.ui.RestaurantDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.response.BootPayChargeReq
import com.example.prepay.data.response.TeamStoreReq
import com.example.prepay.databinding.FragmentDetailRestaurantBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.MainActivityViewModel
import com.example.prepay.util.BootPayManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "DetailRestaurantFragment"
class AddDetailRestaurantFragment: BaseFragment<FragmentDetailRestaurantBinding>(
    FragmentDetailRestaurantBinding::bind,
    R.layout.fragment_detail_restaurant
) {
    private lateinit var mainActivity: MainActivity
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val restaurantDetailsViewModel : RestaurantDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideBottomNav(true)
    }

    override fun onPause() {
        super.onPause()
        mainActivity.hideBottomNav(false)
    }

    private fun initEvent() {

        Log.d(TAG, "restaurantDetailsViewModel: ${restaurantDetailsViewModel.restaurantData.value}")

        binding.requestRestaurantName.text = activityViewModel.storeName.value

        binding.confirm.setOnClickListener {
            val totalPrice = binding.totalPrice.text.toString()
            val storeId = activityViewModel.storeId.value
            val teamId : Long? = activityViewModel.teamId.value
            Log.d(TAG, "initEvent: ")
            Log.d(TAG, "teamId: $teamId")
            BootPayManager.startPayment(requireActivity(), restaurantDetailsViewModel.restaurantData.value.toString(), totalPrice) { receiptId, price ->

                lifecycleScope.launch {
                    try {
                        Log.d(TAG, "storeId: $storeId")
                        Log.d(TAG, "teamId: $teamId, price: ${price}}")
                        Log.d(TAG, "receiptId: $receiptId")

                        val chargeReceipt = storeId?.let { storeId ->
                            teamId?.let { teamId ->
                                BootPayChargeReq(
                                    teamId.toInt(), storeId, totalPrice.toInt(), receiptId)
                            }
                        }
                        val response = withContext(Dispatchers.IO) {
                            delay(1000)
                            chargeReceipt?.let { it ->
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
                    }
                }
            }
        }
    }
}