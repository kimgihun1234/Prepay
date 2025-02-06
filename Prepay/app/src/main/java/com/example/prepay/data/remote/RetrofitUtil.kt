package com.example.prepay

import com.example.prepay.data.remote.UserService


class RetrofitUtil {
    companion object{
        val userService : UserService = ApplicationClass.retrofit.create(UserService::class.java)
    }
}