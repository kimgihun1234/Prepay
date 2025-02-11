package com.example.prepay

import android.app.Application
import com.example.prepay.data.remote.UserService
import com.example.prepay.data.remote.BootPayService
import com.example.prepay.data.remote.OrderService
import com.example.prepay.data.remote.PosService
import com.example.prepay.data.remote.QrService
import com.example.prepay.data.remote.TeamService



class RetrofitUtil {
    companion object{
        val userService : UserService = ApplicationClass.retrofit.create(UserService::class.java)
        val teamService: TeamService = ApplicationClass.retrofit.create(TeamService::class.java)
        val bootPayService: BootPayService = ApplicationClass.retrofit.create(BootPayService::class.java)
        val orderService: OrderService = ApplicationClass.retrofit.create(OrderService::class.java)
        val posService : PosService = ApplicationClass.retrofit.create(PosService::class.java)
        val qrService : QrService = ApplicationClass.retrofit.create(QrService::class.java)
    }
}