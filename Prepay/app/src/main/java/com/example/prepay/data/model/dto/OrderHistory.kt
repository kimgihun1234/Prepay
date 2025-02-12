package com.example.prepay.data.model.dto

data class OrderHistory(
    val orderHistoryId : Int,
    val orderDate: String,
    val totalPrice : Int,
    val refundRequested : Boolean,
    val withdraw : Boolean,
    val companyDinner: Boolean

)

data class OrderHistoryReq(
    val teamId: Long,
    val storeId:Int
)
