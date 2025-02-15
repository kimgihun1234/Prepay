package com.example.prepay.data.response

data class PublicTeamDetailsRes(
    val imageUrl: String,
    val dailyLimit: Int,
    val usedAmount: Int,
    val teamMessage: String,
    val checkLike: Boolean,
    val teamName: String,
    val address : String,
    val teamBalance: Int,
    val latitude : Double,
    val longitude : Double
)

//imageurl, team_balance, teammessage, isLike, teamName, latitude, longitude, address

