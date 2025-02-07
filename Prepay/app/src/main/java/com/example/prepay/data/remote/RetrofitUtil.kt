package com.example.prepay

import com.example.prepay.data.remote.TeamService


class RetrofitUtil {
    companion object{
        val teamService = ApplicationClass.retrofit.create(TeamService::class.java)
    }
}