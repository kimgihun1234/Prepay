package com.example.prepay.ui.GroupSearch

import android.os.Build.VERSION_CODES.P
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.data.response.Team
import com.example.prepay.data.response.TeamUserRes
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

    /*viewModelScope.launch {
    kotlin.runCatching {
        RetrofitUtil.teamService.banUser(1,ban)
    }.onSuccess {
        val currentList = _teamUserListInfo.value?.toMutableList() ?: mutableListOf()
        val updatedList = currentList.filter { it.email != ban.banUserEmail } // 삭제된 사용자를 제외한 리스트
        _teamUserListInfo.value = updatedList
    }.onFailure {

    }
}*/