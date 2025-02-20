package com.example.qrscanner.response

data class OrderDetail(
    val detailPrice: Int,
    val product: String,
    val quantity: Int
)