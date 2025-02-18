package com.example.qrscanner.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.qrscanner.response.TeamRes
import kotlinx.coroutines.launch

private const val TAG = "TeamViewModel"
class TeamViewModel : ViewModel() {
    private val _teamList = MutableLiveData<List<TeamRes>>()
    val teamList : LiveData<List<TeamRes>>
        get() = _teamList

    fun getTeamList(storeId: Int) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.posService.getTeamList(storeId)
            } .onSuccess {
                Log.d(TAG, "getTeamList: 성공")
                _teamList.postValue(it)
            } .onFailure { e->
                Log.d(TAG, "getTeamList: 실패 ${e.message}")
                _teamList.postValue(emptyList())
            }
        }
    }

}