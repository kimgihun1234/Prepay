package com.example.prepay.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    fun setTeamId(teamId: Long) {
        _teamId.postValue(teamId)
    }

    fun setStoreId(storeId: Int) {
        _storeId.postValue(storeId)
    }

    fun setStoreName(storeName: String) {
        _storeName.postValue(storeName)
    }



}