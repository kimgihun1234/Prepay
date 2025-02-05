package com.example.prepay.ui.RestaurantDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentDetailRestaurantBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.util.BootPayManager

private const val TAG = "DetailRestaurantFragmen"
class DetailRestaurantFragment: BaseFragment<FragmentDetailRestaurantBinding>(
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
        val restaurant = ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        Log.d(TAG, "initEvent: ${restaurant.restaurantData.value}")
        binding.requestRestaurantName.text = restaurant.restaurantData.value

        binding.confirm.setOnClickListener {
            val totalPrice = binding.totalPrice.text.toString()
            BootPayManager.startPayment(requireActivity(), restaurant.restaurantData.value.toString(), totalPrice)
        }
    }
}