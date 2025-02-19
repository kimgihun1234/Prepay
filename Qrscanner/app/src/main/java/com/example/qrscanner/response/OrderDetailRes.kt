package com.example.qrscanner.response

data class OrderDetailRes (
    val detailHistoryId: Long,
    val detailPrice: Int,
    val product: String,
    val quantity: Int
    )