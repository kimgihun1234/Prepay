package com.example.prepay.ui.GroupDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.Team
import com.example.prepay.data.response.TeamIdStoreRes
import kotlinx.coroutines.launch

class GroupDetailsFragmentViewModel : ViewModel() {
    private val _storeListInfo = MutableLiveData<List<TeamIdStoreRes>>()
    val storeListInfo: LiveData<List<TeamIdStoreRes>>
        get() = _storeListInfo

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
}