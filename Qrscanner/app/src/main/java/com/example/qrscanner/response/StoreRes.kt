package com.example.qrscanner.response

data class StoreRes (
    val orderHistoryId: Int,
    val orderDate : Int,
    val totalPrice: Int,
    val refundRequested: Boolean,
    val withdraw: Boolean,
    val companyDinner: Boolean
)