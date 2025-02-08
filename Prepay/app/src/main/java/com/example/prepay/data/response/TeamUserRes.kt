package com.example.prepay.data.response


data class TeamUserRes(
    val email : String,
    val nickname: String,
    val position: Boolean,
    val privilege: Boolean,
    val teamId: Int
)