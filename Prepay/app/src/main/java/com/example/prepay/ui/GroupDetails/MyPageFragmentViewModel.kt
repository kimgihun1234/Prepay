package com.example.prepay.ui.GroupDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.Team
import kotlinx.coroutines.launch

class MyPageFragmentViewModel : ViewModel(){
    private val _teamListInfo = MutableLiveData<List<Team>>()
    val teamListInfo: LiveData<List<Team>>
        get() = _teamListInfo

    fun getAllTeamList() {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getTeamList(1)
            }.onSuccess {
                Log.d("MyPageViewModel", "팀 리스트 가져오기 성공: $it")
                _teamListInfo.value = it
            }.onFailure { e ->
                _teamListInfo.value = emptyList()
            }
        }
    }
}