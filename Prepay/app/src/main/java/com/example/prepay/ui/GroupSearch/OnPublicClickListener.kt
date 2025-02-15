package com.example.prepay.ui.GroupSearch

import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamsDisRes

interface OnPublicClickListener {
    fun onGroupClick(publicgroup: PublicTeamsDisRes)
    fun onLikeClick(publicgroup: LikeTeamsReq)
}