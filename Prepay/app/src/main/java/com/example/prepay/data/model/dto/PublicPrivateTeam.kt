package com.example.prepay.data.model.dto

import okhttp3.MultipartBody

data class PublicPrivateTeam (
    val publicTeam : Boolean,
    val teamName : String,
    val dailyPriceLimit : Int,
    val countLimit : Int,
    val teamMessage : String,
    val color : String
) {
    constructor(
        publicTeam: Boolean,
        teamName: String,
        dailyPriceLimit: Int,
        color: String
    ) : this(
        publicTeam,
        teamName,
        dailyPriceLimit,
        0,
        "",
        color
    )
    constructor(
        publicTeam : Boolean,
        teamName : String,
        dailyPriceLimit : Int,
        countLimit : Int,
        teamMessage : String,
        ) : this (
        publicTeam,
        teamName,
        dailyPriceLimit,
        countLimit,
        teamMessage,
        ""
        )
}