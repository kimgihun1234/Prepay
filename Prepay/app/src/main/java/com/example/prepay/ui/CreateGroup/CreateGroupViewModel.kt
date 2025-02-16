package com.example.prepay.ui.CreateGroup

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.StoreAllRes
import com.example.prepay.data.response.TeamIdRes
import kotlinx.coroutines.launch

class CreateGroupViewModel : ViewModel() {

    private val _teamId = MutableLiveData<TeamIdRes>()
    val teamId : LiveData<TeamIdRes>
        get() = _teamId

    private val _storeId = MutableLiveData<Int>()
    val storeId : LiveData<Int>
        get() = _storeId

    private val _colorList = MutableLiveData<List<String>>()
    val colorList : LiveData<List<String>>
        get() = _colorList

    private val _storesListInfo = MutableLiveData<List<StoreAllRes>>()
    val storeListInfo : LiveData<List<StoreAllRes>>
        get() = _storesListInfo

    fun updateTeamId(teamI: TeamIdRes) {
        _teamId.value = teamI
    }
    fun updateStoreId(num : Int) {
        _storeId.value = num
    }

    fun setColorList(context: Context, colorArray: Array<String>) {
        val colorList = mutableListOf<String>()
        for (s in colorArray) {
            // 각 colorName을 리스트에 추가
            if (s.isNotBlank()) {  // 빈 문자열이 아니면
                colorList.add(s)
            }
        }

        _colorList.value = colorList
    }

    fun getAllStore(access: String) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.storeService.getAllStores(access)
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