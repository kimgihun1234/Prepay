package com.example.prepay.data.response

data class TeamDetailRes(
    val countLimit: Int,
    val dailyPriceLimit: Int,
    val position: Boolean,
    val publicTeam: Boolean,
    val teamId: Int,
    val teamMessage: String,
    val teamName: String,
    val teamPassword: Any
)