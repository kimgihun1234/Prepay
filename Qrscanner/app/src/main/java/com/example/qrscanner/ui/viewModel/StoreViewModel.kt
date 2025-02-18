package com.example.qrscanner.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.qrscanner.response.StoreReq
import com.example.qrscanner.response.StoreRes
import kotlinx.coroutines.launch

private const val TAG = "StoreViewModel"
class StoreViewModel : ViewModel() {
    private val _storeList = MutableLiveData<List<StoreRes>>()
    val storeList : LiveData<List<StoreRes>>
        get() = _storeList

    fun getStoreList(store : StoreReq) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.posService.getStoreList(store)
            } .onSuccess {
                _storeList.postValue(it)
                Log.d(TAG, "getStoreList: 성공")
                Log.d(TAG, "getStoreList: $it")
            } .onFailure {e ->
                _storeList.postValue(emptyList())
                Log.d(TAG, "getStoreList: ${e.message}")
                Log.d(TAG, "getStoreList: 실패")
            }
        }
    }

}