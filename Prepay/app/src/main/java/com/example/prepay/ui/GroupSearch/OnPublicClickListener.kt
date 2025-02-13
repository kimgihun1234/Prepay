package com.example.prepay.ui.GroupSearch

import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamsRes

interface OnPublicClickListener {
    fun onGroupClick(publicgroup: PublicTeamsRes)
    fun onLikeClick(publicgroup: LikeTeamsReq)
}