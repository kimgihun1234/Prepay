package com.example.qrscanner.response

data class PosReq(
    val details: List<OrderDetail>,
    val qrUUID: String,
    val storeId: Int,
    val teamId: Int
)