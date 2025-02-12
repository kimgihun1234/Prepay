package com.example.prepay.data.model.dto

data class BootPayCharge(
    val teamId : Int,
    val storeId : Int,
    val amount : Int,
    val receiptId : String
)
