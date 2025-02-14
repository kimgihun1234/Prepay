package com.example.prepay.data.response

data class PublicTeamsDisRes(
    val teamBalance: Int,
    val teamId: Int,
    val teamInitializerNickname: String,
    val teamMessage: String,
    val teamName: String,
    val isLike: Boolean,
    val imageURL: String,
    val latitude: Double,
    val longitude: Double
)
