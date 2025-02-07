package com.example.prepay.ui.GroupSearchDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupSearchtDetailsViewModel: ViewModel() {

    val groupDetailsData = MutableLiveData<String>()

    fun sendGroupDetailsData(name: String) {
        groupDetailsData.value = name
    }
}