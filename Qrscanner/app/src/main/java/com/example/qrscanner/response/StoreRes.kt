package com.example.qrscanner.response

data class StoreRes (
    val orderHistoryId: Int,
    val orderDate : Long,
    val totalPrice: Int,
    val refundRequested: Boolean,
    val storeImgUrl: String,
    val storeName: String,
    val checkPublic : String,
    val nickName: String,
    val withdraw: Boolean,
    val companyDinner: Boolean
)