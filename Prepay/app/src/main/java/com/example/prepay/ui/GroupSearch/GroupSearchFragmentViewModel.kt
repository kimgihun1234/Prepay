package com.example.prepay.ui.GroupSearch


import android.os.Build.VERSION_CODES.P
import android.location.Location
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
import com.example.prepay.data.response.PublicTeamsDisRes
import kotlinx.coroutines.launch

private const val TAG = "GroupSearchFragmentView"
class GroupSearchFragmentViewModel : ViewModel(){
    private val _getPublicTeams = MutableLiveData<List<PublicTeamsRes>>()
    val getPublicTeams: LiveData<List<PublicTeamsRes>>
        get() = _getPublicTeams

    private val _sortDistancePublicTeams = MutableLiveData<List<PublicTeamsDisRes>>()
    val sortDistancePublicTeams : LiveData<List<PublicTeamsDisRes>>
        get() = _sortDistancePublicTeams

    private val _userLocation = MutableLiveData<Location>()
    val userLocation: LiveData<Location> get() = _userLocation



    val email = "user1@gmail.com"
//    fun getAllPublicTeamList() {
//        viewModelScope.launch {
//            runCatching {
//                RetrofitUtil.teamService.getPublicTeams(email)
//            }.onSuccess {
//                Log.d("GroupSearchViewModel", "공개 팀 리스트 가져오기 성공: $it")
//                _getPublicTeams.value = it
//            }.onFailure { e ->
//                Log.d("GroupSearchViewModel", "getAllPublicTeamList: 실패")
//                _getPublicTeams.value = emptyList()
//            }
//        }
//    }


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

    fun getSortDistancePublicTeamList(latitude : Double, longitude : Double) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getTeamStoreDistance(email, latitude, longitude)
            }.onSuccess {
                Log.d("GroupSearchViewModel", "공개 팀 리스트 가져오기 성공: $it")
                _sortDistancePublicTeams.value = it
            }.onFailure { e ->
                Log.d("GroupSearchViewModel", "getAllPublicTeamList: 실패")
                _sortDistancePublicTeams.value = emptyList()
            }
        }
    }

//    private fun sortDistance(teams: List<PublicTeamsDisRes>) :List<PublicTeamsDisRes> {
//        Log.d(TAG, "sortDistance: ${userLocation.value?.latitude}")
//        Log.d(TAG, "sortDistance: ${userLocation.value?.longitude}")
//        val userLat = userLocation.value?.latitude
//        val userLon = userLocation.value?.longitude
//
//        // 유저 위치가 없으면 정렬하지 않고 원본 리스트 반환
//        if (userLat == null || userLon == null) {
//            Log.e(TAG, "User location is null")
//            return teams
//        }
//
//        // 거리 계산 후 가까운 순으로 정렬
//        return teams.sortedBy { team ->
//            calculateDistance(userLat, userLon, team.latitude, team.longitude)
//        }.also { sortedTeams ->
//            sortedTeams.forEach { team ->
//                Log.d(TAG, "Sorted Team: ${team.teamName}, Distance: ${
//                    calculateDistance(userLat, userLon, team.latitude, team.longitude)
//                } km")
//            }
//        }
//    }

//    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//        val R = 6371.0 // 지구 반경 (단위: km)
//        val dLat = Math.toRadians(lat2 - lat1)
//        val dLon = Math.toRadians(lon2 - lon1)
//
//        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                Math.sin(dLon / 2) * Math.sin(dLon / 2)
//        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
//        Log.d(TAG, "calculateDistance: ${R*c}")
//        return R * c // 두 지점 간의 거리 (단위: km)
//    }

    fun updateLocation(location: Location) {
        _userLocation.value = location
    }
}
