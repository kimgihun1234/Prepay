package com.example.prepay.ui.GroupSearchDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupSearchtDetailsViewModel : ViewModel() {
    val groupName: MutableLiveData<String> = MutableLiveData()
    val groupLeftMoney: MutableLiveData<Int> = MutableLiveData()
    val groupImageURL: MutableLiveData<String> = MutableLiveData()

    // 데이터를 설정하는 메서드
    fun sendGroupDetails(name: String, leftMoney: Int, imageURL: String) {
        groupName.value = name
        groupLeftMoney.value = leftMoney
        groupImageURL.value = imageURL
    }

    private val _isLiked = MutableLiveData<Boolean>(false)
    val isLiked: LiveData<Boolean> get() = _isLiked

    fun toggleLike() {
        _isLiked.value = _isLiked.value?.not()
    }
}