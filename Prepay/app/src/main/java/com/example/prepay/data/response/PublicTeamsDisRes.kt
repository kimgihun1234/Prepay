package com.example.prepay.data.response

import com.example.prepay.ui.GroupSearch.Searchable

data class PublicTeamsDisRes(
    val teamId: Int,
    val teamName: String,
    val teamMessage: String,
    val teamBalance: Int,
    val teamInitializerNickname: String,
    val imgUrl: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Double,
    val like: Boolean,
    val address: String
) : Searchable {
    override val searchableText : String
        get() = "$teamName"
}

