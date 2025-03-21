package com.example.prepay.ui.GroupDetails

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil

import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.data.model.dto.Restaurant
import com.example.prepay.data.response.BanUserReq
import com.example.prepay.data.response.OrderHistoryReq
 import com.example.prepay.data.response.StoreIdReq
import com.example.prepay.data.response.StoreIdRes
import com.example.prepay.data.response.Team
import com.example.prepay.data.response.TeamDetailRes
import com.example.prepay.data.response.StoreRes
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


    private val _storesListInfo = MutableLiveData<List<StoreRes>>()
    val storesListInfo: LiveData<List<StoreRes>>
        get() = _storesListInfo

    private val _userposition = MutableLiveData<Boolean>()
    val userposition:LiveData<Boolean> get() = _userposition


    private val _teamOrderListInfo = MutableLiveData<List<OrderHistory>>()
    val teamOrderListInfo: LiveData<List<OrderHistory>>
        get() = _teamOrderListInfo


    private val _teamDetail = MutableLiveData<TeamDetailRes>().apply {
        value = TeamDetailRes(
            countLimit = 0,
            dailyPriceLimit = 0,
            position = false,
            publicTeam = false,
            teamId = 0,
            usedAmount = 0,
            teamMessage = "",
            teamName = "",
            teamPassword = "",
            color = "#FFFFFF",
            teamBalance = 0
        )
    }

    val teamDetail: LiveData<TeamDetailRes> get() = _teamDetail

    /** 전체 데이터를 업데이트하는 함수 */
    fun getTeamDetail(access:String,teamId: Long) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getTeamDetails(access,teamId)
            }.onSuccess {
                Log.d("getMyTeam", "전체 데이터를 업데이트 합니다: $it")
                _teamDetail.value = it
            }.onFailure { e ->

            }
        }
    }

    //장소 변했을 때 값변경
    fun updateLocation(location: Location) {
        _userLocation.value = location
    }

    fun updatePosition(userposition: Boolean){
        _userposition.value = userposition
    }
    fun getMyTeamRestaurantList(access:String,teamId: Long) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getStoreOfTeam(access,teamId)
            }.onSuccess {
                Log.d("getMyTeam", "팀 리스트 가져오기 성공: $it")
                _storeListInfo.value = it
            }.onFailure { e ->
                _storeListInfo.value = emptyList()
            }
        }
    }

    fun getMyTeamOrderHistory(access: String, rq: OrderHistoryReq){
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.orderService.getDetailHistory(access, rq)
            } .onSuccess { response ->
                _teamOrderListInfo.postValue(response)
                Log.d("OrderHistoryList", "getAllOrderHistoryList: 팀리스트 가져오기 성공: $response")
            } .onFailure { e ->
                Log.d("getAllOrderHistoryList","getAllOrderHistoryList: ${e.message}")
                _teamOrderListInfo.value = emptyList()
            }
        }
    }

    fun TeamResign(access:String,ban: BanUserReq){
        viewModelScope.launch {
            kotlin.runCatching {
                RetrofitUtil.teamService.banUser(access,ban)
            }.onSuccess {
                val currentList = _teamUserListInfo.value?.toMutableList() ?: mutableListOf()
                val updatedList = currentList.filter { it.email != ban.banUserEmail } // 삭제된 사용자를 제외한 리스트
                _teamUserListInfo.value = updatedList
            }.onFailure {

            }
        }
    }

    fun getMyTeamUserList(access:String,teamId: Long) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getUserOfTeam(access, teamId)
            }.onSuccess {
                Log.d("getMyTeam", "팀 리스트 가져오기 성공: $it")
                _teamUserListInfo.value = it
            }.onFailure { e ->
                _teamUserListInfo.value = emptyList()
            }
        }
    }
    fun getStoreId(access:String, teamId: Long) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.storeService.getStores(access, teamId)
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