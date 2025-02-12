package com.example.qrscanner.response

data class PosReq(
    val details: List<orderDetail>,
    val email: String,
    val qrUUID: String,
    val storeId: Int,
    val teamId: Int
)