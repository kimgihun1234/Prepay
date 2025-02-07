package com.example.prepay.data.model.dto

import okhttp3.MultipartBody

data class PublicPrivateTeam (
    val publicTeam : Boolean,
    val teamName : String,
    val dailyPriceLimit : Int,
    val countLimit : Int,
    val teamMessage : String,
) {
    constructor(
        publicTeam: Boolean,
        teamName: String,
        dailyPriceLimit: Int,
    ) : this(
        publicTeam,
        teamName,
        dailyPriceLimit,
        0,
        "",
    )
}
data class ImageRequest (
    val imageUrl: MultipartBody.Part?
)


