package com.example.qrscanner.response

data class TeamRes (
    val teamId : Int,
    val teamName : String,
    val storeBalance: Int,
    val imgUrl : String,
    val publicTeam: Boolean,
    val email: String,
    val nickname: String,
    val teamMessage: String,
    val dailyPriceLimit: Int
)