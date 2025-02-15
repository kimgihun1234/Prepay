package com.example.prepay.ui.GroupSearch

import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicLikeRes


interface OnPublicLikeClickListener {
    fun onPublicGroupClick(publicGroupLike: PublicLikeRes)
    fun onPublicLikeClick(publicLike: LikeTeamsReq)
}
