package com.example.prepay.data.response

data class Team(
    val teamId: Int,
    val teamName: String,
    val publicTeam: Boolean,
    val balance: Int,
    val color : String
)