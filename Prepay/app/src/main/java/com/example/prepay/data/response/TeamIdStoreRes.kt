package com.example.prepay.data.response

data class TeamIdStoreRes(
    val balance: Int,
    val storeId: Int,
    val storeName: String,
    val latitude: Double,
    val longitude: Double,
    val imgUrl: String,
)