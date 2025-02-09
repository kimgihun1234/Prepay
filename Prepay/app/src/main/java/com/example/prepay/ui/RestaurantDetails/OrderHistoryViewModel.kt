package com.example.prepay.ui.RestaurantDetails


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.data.model.dto.OrderHistoryReq
import kotlinx.coroutines.launch

class OrderHistoryViewModel : ViewModel() {
    private val _orderHistoryListInfo = MutableLiveData<List<OrderHistory>>()
    val orderHistoryListInfo : LiveData<List<OrderHistory>>
        get() = _orderHistoryListInfo

    fun getAllOrderHistoryList() {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.orderService.getDetailHistory(1, OrderHistoryReq(1,0))
            } .onSuccess { response ->
                _orderHistoryListInfo.postValue(response)
                Log.d("OrderHistoryList", "getAllOrderHistoryList: 팀리스트 가져오기 성공: $response")
            } .onFailure { e ->
                Log.d("getAllOrderHistoryList","getAllOrderHistoryList: ${e.message}")
                _orderHistoryListInfo.value = emptyList()
            }
        }
    }

}