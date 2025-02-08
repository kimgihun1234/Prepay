package com.example.prepay.ui.GroupDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.Team
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.data.response.TeamUserRes
import kotlinx.coroutines.launch

class GroupDetailsFragmentViewModel : ViewModel() {
    private val _storeListInfo = MutableLiveData<List<TeamIdStoreRes>>()
    val storeListInfo: LiveData<List<TeamIdStoreRes>>
        get() = _storeListInfo

    private val _teamUserListInfo = MutableLiveData<List<TeamUserRes>>()
    val teamUserListInfo: LiveData<List<TeamUserRes>>
        get() = _teamUserListInfo

    fun getMyTeamRestaurantList(userId:Long,teamId: Long) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getStoreOfTeam(userId,teamId)
            }.onSuccess {
                Log.d("getMyTeam", "팀 리스트 가져오기 성공: $it")
                _storeListInfo.value = it
            }.onFailure { e ->
                _storeListInfo.value = emptyList()
            }
        }
    }

    fun getMyTeamUserList(userId:Long,teamId: Long) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getUserOfTeam(userId, teamId)
            }.onSuccess {
                Log.d("getMyTeam", "팀 리스트 가져오기 성공: $it")
                _teamUserListInfo.value = it
            }.onFailure { e ->
                _teamUserListInfo.value = emptyList()
            }
        }
    }
}