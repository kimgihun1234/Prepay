package com.example.prepay.ui.GroupDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.prepay.BaseFragment
import com.example.prepay.R
import com.example.prepay.databinding.FragmentAddRestaurantBinding
import com.example.prepay.ui.MainActivity
import com.example.prepay.ui.RestaurantDetails.RestaurantViewModel
import com.example.prepay.util.BootPayManager

private const val TAG = "AddRestaurantFragment"
class AddRestaurantFragment : BaseFragment<FragmentAddRestaurantBinding>(
    FragmentAddRestaurantBinding::bind,
    R.layout.fragment_add_restaurant
) {
    private lateinit var mainActivity: MainActivity
    private var restaurantName: String = ""

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
        restaurant.restaurantData.value
        Log.d(TAG, "initEvent: ${restaurant.restaurantData.value}")


        binding.restaurantName
        binding.restaurantName.isSubmitButtonEnabled = true
        binding.restaurantName.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                restaurantName = query.toString()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        binding.confirm.setOnClickListener {
            val totalPrice = binding.totalPrice.text.toString()
            BootPayManager.startPayment(requireActivity(), restaurantName, totalPrice)
        }
    }
}