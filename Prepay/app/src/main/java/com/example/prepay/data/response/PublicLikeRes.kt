package com.example.prepay.data.response

import com.example.prepay.ui.GroupSearch.Searchable

data class PublicLikeRes(
    val teamId: Int,
    val address: String,
    val teamBalance: Int,
    val checkLike: Boolean,
    val dailyLimit: Int,
    val imageUrl: String,
    val teamMessage: String,
    val teamName: String,
    val usedAmount: Int,
    val distance : Double
) : Searchable {
    override val searchableText : String
        get() = "$teamName"
}