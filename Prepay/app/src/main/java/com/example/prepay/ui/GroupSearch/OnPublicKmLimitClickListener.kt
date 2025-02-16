package com.example.prepay.ui.GroupSearch

import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.PublicTeamsDisRes

interface OnPublicKmLimitClickListener {
    fun onKmLimitGroupClick(publicgroup: PublicTeamsDisRes)
    fun onKmLimitLikeGroupClick(publicgroup: LikeTeamsReq)
}