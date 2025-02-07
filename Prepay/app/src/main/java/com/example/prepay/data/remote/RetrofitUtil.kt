package com.example.prepay

import com.example.prepay.data.remote.BootpayService
import com.example.prepay.data.remote.FcmService
import com.example.prepay.data.remote.OrderService
import com.example.prepay.data.remote.PosService
import com.example.prepay.data.remote.TeamService


class RetrofitUtil {
    companion object{
        val teamService: TeamService = ApplicationClass.retrofit.create(TeamService::class.java)
        val fcmService :FcmService = ApplicationClass.retrofit.create(FcmService::class.java)
        val orderService : OrderService = ApplicationClass.retrofit.create(OrderService::class.java)
        val posService : PosService = ApplicationClass.retrofit.create(PosService::class.java)
        val bootpayService : BootpayService = ApplicationClass.retrofit.create(BootpayService::class.java)
    }
}