package com.example.prepay.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.PublicTeamsRes
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel()  {
    private val _teamId = MutableLiveData<Long>()
    val teamId: LiveData<Long>
        get() = _teamId

    private val _storeId = MutableLiveData<Int>()
    val storeId : LiveData<Int>
        get() = _storeId

    private val _storeName = MutableLiveData<String>()
    val storeName : LiveData<String>
        get() = _storeName

    private val _userName = MutableLiveData<String>()
    val userName : LiveData<String>
        get() = _userName

    fun setTeamId(teamId: Long) {
        _teamId.postValue(teamId)
    }

    fun setStoreId(storeId: Int) {
        _storeId.postValue(storeId)
    }

    fun setStoreName(storeName: String) {
        _storeName.postValue(storeName)
    }

    fun setUserName(userName : String) {
        _userName.postValue(userName)
    }
}