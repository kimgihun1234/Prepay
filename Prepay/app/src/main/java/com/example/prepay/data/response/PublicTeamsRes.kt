package com.example.prepay.data.response

import com.example.prepay.ui.GroupSearch.Searchable

data class PublicTeamsRes(
    val teamId: Int,
    val teamName: String,
    val teamMessage: String,
    val teamBalance: Int,
    val teamInitializerNickname: String,
    val like: Boolean,
    val imgUrl: String,
    val address: String,
    val distance : Double
) : Searchable {
    override val searchableText : String
        get() = "$teamName"
}