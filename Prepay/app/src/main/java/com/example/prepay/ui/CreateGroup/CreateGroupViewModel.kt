package com.example.prepay.ui.CreateGroup

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.prepay.data.response.TeamIdRes

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
}