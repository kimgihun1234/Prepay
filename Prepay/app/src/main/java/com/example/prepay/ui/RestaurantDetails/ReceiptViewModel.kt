package com.example.prepay.ui.RestaurantDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.Receipt
import kotlinx.coroutines.launch

class ReceiptViewModel : ViewModel() {
    private val _receiptListInfo = MutableLiveData<List<Receipt>>()
    val receiptListInfo : LiveData<List<Receipt>>
        get() = _receiptListInfo

    fun getAllReceiptList(detailHistoryId: Int, access:String) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.orderService.getDetailReceipt(detailHistoryId, access)
            } .onSuccess { response ->
                _receiptListInfo.postValue(response)
                Log.d("ReceiptList", "getDetailReceiptList: 영수증리스트 가져오기 $response")
            } .onFailure {
                _receiptListInfo.value = emptyList()
            }
        }
    }
}