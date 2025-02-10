package com.example.prepay.ui.RestaurantDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.prepay.BaseFragment
import com.example.prepay.CommonUtils
import com.example.prepay.R
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.BootPayCharge
import com.example.prepay.databinding.FragmentDetailRestaurantBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.util.BootPayManager
import com.example.prepay.util.RequestBootPayManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "DetailRestaurantFragment"
class AddDetailRestaurantFragment: BaseFragment<FragmentDetailRestaurantBinding>(
    FragmentDetailRestaurantBinding::bind,
    R.layout.fragment_detail_restaurant
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
        val restaurant = ViewModelProvider(requireActivity()).get(RestaurantDetailsViewModel::class.java)

        Log.d(TAG, "initEvent: ${restaurant.restaurantData.value}")
        binding.requestRestaurantName.text = restaurant.restaurantData.value

        binding.confirm.setOnClickListener {
            val totalPrice = binding.totalPrice.text.toString()

            RequestBootPayManager.startPayment(requireActivity(), restaurant.restaurantData.value.toString(), totalPrice) { teamId, storeId ->
                lifecycleScope.launch {
                    try {
                        val chargeReceipt = BootPayCharge(1, 1, totalPrice.toInt(), "")
                        val response = withContext(Dispatchers.IO) {
                            RetrofitUtil.bootPayService.getBootPay(chargeReceipt)
                        }

                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "영수증이 성공적으로 들어갔습니다.", Toast.LENGTH_SHORT).show()
                            // 영수증 올리고 그다음 프레그먼트 이동
                            mainActivity.changeFragmentMain(CommonUtils.MainFragmentName.MYPAGE_FRAGMENT)

                        } else {
                            Log.e(TAG, "영수증 올리기 실패: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "네트워크 요청 실패: ${e.localizedMessage}", e)
                    }
                }
            }
        }
    }
}