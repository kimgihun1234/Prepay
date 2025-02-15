package com.example.prepay.ui.GroupSearch

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.SharedPreferencesUtil
import com.example.prepay.data.response.PublicLikeRes
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.data.response.PublicTeamsDisRes
import kotlinx.coroutines.launch
import retrofit2.http.Query

private const val TAG = "GroupSearchFragmentView"
class GroupSearchFragmentViewModel : ViewModel(){
    private val _getPublicTeams = MutableLiveData<List<PublicTeamsRes>>()
    val getPublicTeams: LiveData<List<PublicTeamsRes>>
        get() = _getPublicTeams

    private val _sortDistancePublicTeams = MutableLiveData<List<PublicTeamsDisRes>>()
    val sortDistancePublicTeams : LiveData<List<PublicTeamsDisRes>>
        get() = _sortDistancePublicTeams

    private val _getPublicLikeTeams = MutableLiveData<List<PublicLikeRes>>()
    val getPublicLikeTeams: LiveData<List<PublicLikeRes>>
        get() = _getPublicLikeTeams

    private val _userLocation = MutableLiveData<Location>()
    val userLocation: LiveData<Location> get() = _userLocation


    fun getPublicTeamList(){
        viewModelScope.launch{
            runCatching {
                RetrofitUtil.teamService.getPublicTeams(SharedPreferencesUtil.getAccessToken()!!)
            }.onSuccess {
                _getPublicTeams.value = it
            }.onFailure {

            }
        }
    }

    fun getTeamLikeList(latitude : Double, longitude:Double){
        viewModelScope.launch{
            runCatching {
                RetrofitUtil.teamService.getlikeTeamList(SharedPreferencesUtil.getAccessToken()!!,latitude,longitude)
            }.onSuccess {
                _getPublicLikeTeams.value = it
            }.onFailure {

            }
        }
    }


    fun getSortDistancePublicTeamList(latitude : Double, longitude : Double) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.getTeamStoreDistance(SharedPreferencesUtil.getAccessToken()!!, latitude, longitude)
            }.onSuccess {
                Log.d("GroupSearchViewModel", "공개 팀 리스트 가져오기 성공: $it")
                _sortDistancePublicTeams.value = it
            }.onFailure { e ->
                Log.d("GroupSearchViewModel", "getAllPublicTeamList: 실패")
                _sortDistancePublicTeams.value = emptyList()
            }
        }
    }

    fun updateLocation(location: Location) {
        _userLocation.value = location
    }
}
