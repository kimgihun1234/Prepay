package com.example.prepay.data.response

data class GetUserOfTeamRes(
    val nickName: String,
    val position: Boolean,
    val privilege: Boolean,
    val teamId: Int,
    val userName: String
)