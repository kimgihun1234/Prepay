package com.example.prepay.data.model.dto

data class Public (
    val pk: Int,
    val name :String,
    val address: String,
    val distance: Int,
    val leftMoney: Int,
    val imageURL: String
    )