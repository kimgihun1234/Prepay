package com.example.prepay.ui.GroupDetails

import com.example.prepay.User

interface OnTeamUserActionListener {
    fun onManageClick(user: User)
    fun onResignClick(user: User)
}