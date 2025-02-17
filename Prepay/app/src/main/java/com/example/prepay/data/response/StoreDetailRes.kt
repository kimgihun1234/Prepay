package com.example.prepay.data.response

data class StoreDetailRes(
    val balance: Int,
    val latitude: Double,
    val longitude: Double,
    val storeDescription: String,
    val storeImgUrl: String,
    val storeName: String
)