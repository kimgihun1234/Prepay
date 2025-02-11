package com.example.prepay.ui.CreateGroup

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.TeamIdReq
import com.example.prepay.data.response.TeamIdRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateGroupViewModel : ViewModel() {

    private val _teamId = MutableLiveData<TeamIdRes>()
    val teamId : LiveData<TeamIdRes>
        get() = _teamId

    private val _storeId = MutableLiveData<Int>()
    val storeId : LiveData<Int>
        get() = _storeId

    fun updateTeamId(teamI: TeamIdRes) {
        _teamId.value = teamI
    }
    fun updateStoreId(num : Int) {
        _storeId.value = num
    }
}