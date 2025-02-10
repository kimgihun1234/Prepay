package com.example.prepay.data.response

data class PublicTeamsRes(
    val teamBalance: Int,
    val teamId: Int,
    val teamInitializerNickname: String,
    val teamMessage: String,
    val teamName: String
)