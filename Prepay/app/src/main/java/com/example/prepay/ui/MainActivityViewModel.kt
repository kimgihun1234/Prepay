package com.example.prepay.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel()  {
    private val _teamId = MutableLiveData<Long>()
    val teamId: LiveData<Long>
        get() = _teamId

    fun setTeamId(teamId: Long) {
        _teamId.postValue(teamId)
    }

}