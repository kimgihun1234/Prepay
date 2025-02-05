package com.example.prepay.ui.RestaurantDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantViewModel: ViewModel() {

    val restaurantData = MutableLiveData<String>()

    fun sendRestaurantData(name: String) {
        restaurantData.value = name
    }
}