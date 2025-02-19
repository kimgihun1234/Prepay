package com.example.qrscanner.response

data class OrderDetailRes (
    val detailHistoryId: Long,
    val product: String,
    val detailPrice: Int,
    val quantity: Int
    )