package com.example.qrscanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private val _teamId = MutableLiveData<Int>()
    val teamId : LiveData<Int>
        get() = _teamId

    private val _storeId = MutableLiveData<Int>()
    val storeId: LiveData<Int>
        get() = _storeId

    fun setStoreId(storeId: Int) {
        _storeId.value = storeId
    }

    fun setTeamId(teamId : Int) {
        _teamId.value = teamId
    }

}