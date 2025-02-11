package com.example.prepay.ui.RestaurantDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.data.model.dto.RestaurantData

class RestaurantDetailsViewModel: ViewModel() {

    private val _restaurantData = MutableLiveData<RestaurantData>()
    val restaurantData : LiveData<RestaurantData>
        get() = _restaurantData

    fun setRestaurantData(restaurantData: RestaurantData) {
        _restaurantData.value = restaurantData
    }
}