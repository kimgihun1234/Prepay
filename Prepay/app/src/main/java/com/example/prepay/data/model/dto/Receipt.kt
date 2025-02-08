package com.example.prepay.data.model.dto

data class Receipt(
    val detailHistoryId : Int,
    val product : String,
    val detailPrice : Int,
    val quantity : Int
)