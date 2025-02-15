package com.example.prepay.data.response

data class OrderHistoryRes(
    val orderHistoryId : Int,
    val orderDate: Long,
    val totalPrice: Int,
    val refundRequested: Boolean,
    val storeImgUrl: String,
    val storeName: String,
    val checkPublic: Boolean,
    val withdraw: Boolean,
    val companyDinner: Boolean
)
