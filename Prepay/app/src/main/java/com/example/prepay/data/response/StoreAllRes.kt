package com.example.prepay.data.response

data class StoreAllRes (
    val storeId: Int,
    val storeName: String,
    val address: String,
    val type: String,
    val longitude: Double,
    val latitude: Double,
    val storeImgUrl: String,
    val storeDescription: String
)