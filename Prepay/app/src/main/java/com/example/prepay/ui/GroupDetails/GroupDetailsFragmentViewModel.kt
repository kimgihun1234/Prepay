package com.example.prepay.ui.GroupDetails

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.data.response.BanUserReq
import com.example.prepay.data.response.StoreIdReq
import com.example.prepay.data.response.StoreIdRes
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

    private val _userLocation = MutableLiveData<Location>()
    val userLocation: LiveData<Location> get() = _userLocation

    private val _storesListInfo = MutableLiveData<List<StoreIdRes>>()
    val storesListInfo: LiveData<List<StoreIdRes>>
        get() = _storesListInfo

    fun updateLocation(location: Location) {
        _userLocation.value = location
    }

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

    fun TeamResign(ban: BanUserReq){
        viewModelScope.launch {
            kotlin.runCatching {
                RetrofitUtil.teamService.banUser(1,ban)
            }.onSuccess {
                val currentList = _teamUserListInfo.value?.toMutableList() ?: mutableListOf()
                val updatedList = currentList.filter { it.email != ban.banUserEmail } // 삭제된 사용자를 제외한 리스트
                _teamUserListInfo.value = updatedList
            }.onFailure {

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
    fun getStoreId(storeReq : StoreIdReq) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.storeService.getStores(storeReq, "user1@gmail.com")
            } .onSuccess {
                Log.d("StoreId", "스토어 값들 가져오기 성공: $it")
                _storesListInfo.value = it
                Log.d("getStoreId", "getStoreId: ${_storesListInfo.value}")
            } .onFailure { e ->
                Log.d("storeId", "스토어 값 가져오기 실패")
                _storesListInfo.value = emptyList()
                Log.d("getStoreId", "getStoreId: ${_storesListInfo.value}")
            }
        }
    }
}