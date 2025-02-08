package com.example.prepay.ui.GroupDetails

import com.example.prepay.data.response.TeamUserRes

interface OnTeamUserActionListener {
    fun onManageClick(teamUserRes: TeamUserRes)
    fun onResignClick(teamUserRes: TeamUserRes)
}