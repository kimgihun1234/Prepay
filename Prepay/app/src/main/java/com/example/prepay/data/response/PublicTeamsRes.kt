package com.example.prepay.data.response

data class PublicTeamsRes(
    val teamId: Int,
    val teamName: String,
    val teamMessage: String,
    val teamBalance: Int,
    val teamInitializerNickname: String,
    val like: Boolean,
    val imgUrl: String
)