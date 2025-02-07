package com.example.prepay

import com.example.prepay.data.remote.TeamService


class RetrofitUtil {
    companion object{

        val teamService: TeamService = ApplicationClass.retrofit.create(TeamService::class.java)

    }
}