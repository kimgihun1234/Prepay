package com.example.prepay.ui.GroupSearch

import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamsDisRes
import com.example.prepay.data.response.PublicTeamsRes

interface OnPublicClickListener {
    fun onGroupClick(publicgroup: PublicTeamsDisRes)
    fun onLikeClick(publicgroup: LikeTeamsReq)
}