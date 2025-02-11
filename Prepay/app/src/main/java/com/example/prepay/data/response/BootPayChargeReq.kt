package com.example.prepay.data.response

data class BootPayChargeReq(
    val teamId: Int,
    val storeId: Int,
    val amount: Int,
    val receiptId: String
)
