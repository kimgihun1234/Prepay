package com.example.prepay.ui.RestaurantDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.data.model.dto.RestaurantData
import com.example.prepay.data.response.StoreDetailRes
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel: ViewModel() {

    private val _restaurantData = MutableLiveData<RestaurantData>()
    val restaurantData : LiveData<RestaurantData>
        get() = _restaurantData

    fun setRestaurantData(restaurantData: RestaurantData) {
        _restaurantData.value = restaurantData
    }

    private val _teamStoreDetail = MutableLiveData<StoreDetailRes>()
    val teamStoreDetail : LiveData<StoreDetailRes>
        get() = _teamStoreDetail

    fun getTeamStoreDetail(access: String,teamId: Int,storeId: Int){
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getTeamStoreDetail(access,teamId,storeId)
            } .onSuccess { response ->
                _teamStoreDetail.postValue(response)
                Log.d("ReceiptList", "getDetailReceiptList: 영수증리스트 가져오기 $response")
            } .onFailure {e ->
                Log.e("싸피", "실패했습니다: ${e.message}", e)
            }
        }
    }

}