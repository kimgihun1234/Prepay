package com.example.prepay.data.response

data class StoreRes (
    val storeId: Int,
    val storeName: String,
    val balance: Int,
    val latitude: Double,
    val longitude: Double,
    val myteam: Boolean
)