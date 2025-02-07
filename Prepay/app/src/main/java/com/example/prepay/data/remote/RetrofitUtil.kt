package com.example.prepay

import com.example.prepay.data.remote.BootPayService
import com.example.prepay.data.remote.TeamService


class RetrofitUtil {
    companion object{

        val teamService: TeamService = ApplicationClass.retrofit.create(TeamService::class.java)
        val bootPayService: BootPayService = ApplicationClass.retrofit.create(BootPayService::class.java)
    }
}