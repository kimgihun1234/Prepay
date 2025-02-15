package com.example.prepay.data.response

data class PrivilegeUserReq(
    val changeUserEmail: String,
    val privilege: Boolean,
    val teamId: Int
)