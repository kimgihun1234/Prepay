package com.example.prepay.ui.GroupSearch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.data.response.Team
import kotlinx.coroutines.launch

class GroupSearchFragmentViewModel : ViewModel(){
    private val _getPublicTeams = MutableLiveData<List<PublicTeamsRes>>()
    val getPublicTeams: LiveData<List<PublicTeamsRes>>
        get() = _getPublicTeams

    val email = "user1@gmail.com"
    fun getAllPublicTeamList() {

        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getPublicTeams(email)
            }.onSuccess {
                Log.d("GroupSearchViewModel", "공개 팀 리스트 가져오기 성공: $it")
                _getPublicTeams.value = it
            }.onFailure { e ->
                Log.d("GroupSearchViewModel", "getAllPublicTeamList: 실패")
                _getPublicTeams.value = emptyList()
            }
        }
    }
}