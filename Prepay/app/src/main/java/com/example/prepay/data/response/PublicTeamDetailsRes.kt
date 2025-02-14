package com.example.prepay.data.response

data class PublicTeamDetailsRes(
    val imageURL: String,
    val dailyLimit: Int,
    val usedAmount: Int,
    val teamMessage: String,
    val checkLike: Boolean,
    val teamName: String
)