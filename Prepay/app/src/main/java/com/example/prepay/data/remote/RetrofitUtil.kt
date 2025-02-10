package com.example.prepay

import com.example.prepay.data.remote.UserService
import com.example.prepay.data.remote.BootPayService
import com.example.prepay.data.remote.OrderService
import com.example.prepay.data.remote.FcmService
import com.example.prepay.data.remote.PosService
import com.example.prepay.data.remote.TeamService



class RetrofitUtil {
    companion object{
        val userService : UserService = ApplicationClass.retrofit.create(UserService::class.java)
        val teamService: TeamService = ApplicationClass.retrofit.create(TeamService::class.java)
        val bootPayService: BootPayService = ApplicationClass.retrofit.create(BootPayService::class.java)
        val orderService: OrderService = ApplicationClass.retrofit.create(OrderService::class.java)
        val fcmService :FcmService = ApplicationClass.retrofit.create(FcmService::class.java)
        val posService : PosService = ApplicationClass.retrofit.create(PosService::class.java)
    }
}