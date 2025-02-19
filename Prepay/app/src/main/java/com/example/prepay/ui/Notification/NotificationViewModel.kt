package com.example.prepay.ui.Notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.response.OrderHistoryRes
import kotlinx.coroutines.launch

private const val TAG = "NotificationViewModel"
class NotificationViewModel : ViewModel() {

    private val _myOrderHistoreList = MutableLiveData<List<OrderHistoryRes>>()
    val myOrderHistoryList : LiveData<List<OrderHistoryRes>>
        get() = _myOrderHistoreList

    fun getMyOrderHistoryList() {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.orderService.getMyHistoryList(SharedPreferencesUtil.getAccessToken()!!)
            } .onSuccess {
                _myOrderHistoreList.value = it
            } .onFailure {e ->
                Log.d(TAG, "getMyOrderHistoryList: ${e.message}")
            }
        }
    }
}