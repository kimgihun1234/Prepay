package com.example.prepay.ui.GroupSearchDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prepay.RetrofitUtil
import com.example.prepay.data.response.LikeTeamsReq
import kotlinx.coroutines.launch

class GroupSearchtDetailsViewModel : ViewModel() {
    val groupId: MutableLiveData<Int> = MutableLiveData()
    val groupName: MutableLiveData<String> = MutableLiveData()
    val groupMessage: MutableLiveData<String> = MutableLiveData()
    val groupLeftMoney: MutableLiveData<Int> = MutableLiveData()
    val groupConstructor: MutableLiveData<String> = MutableLiveData()
    val groupImageURL: MutableLiveData<String> = MutableLiveData()

    // 데이터를 설정하는 메서드
    fun sendGroupDetails(id: Int, name: String, message: String, leftMoney: Int, constructor: String, imageURL: String?) {
        groupId.value = id
        groupName.value = name
        groupMessage.value = message
        groupLeftMoney.value = leftMoney
        groupConstructor.value = constructor
        groupImageURL.value = imageURL ?: "https://fastly.picsum.photos/id/110/200/300.jpg?hmac=IMeVMiaNHiAeyVJRyiLHVnYMW0UpSBDkkUtSO1gIidQ"
    }

    private val _isLiked = MutableLiveData<Boolean>().apply { value = false }
    val isLiked: LiveData<Boolean> get() = _isLiked

    fun toggleLike() {
        _isLiked.value = _isLiked.value?.not()
    }

    fun sendLikeStatus(email: String, info: LikeTeamsReq) {
        val currentStatus = _isLiked.value ?: false


        viewModelScope.launch {
            runCatching {
                RetrofitUtil.teamService.sendLikeStatus(email, info)
            }.onSuccess {
                Log.d("LikeStatus", "좋아요 정보 보내기 완료, 현재 상태 : $currentStatus")
            }.onFailure {
                Log.e("LikeStatus", "전송 실패", it)
            }
        }
    }
}