package com.example.prepay

import com.example.qrscanner.base.ApplicationClass
import com.example.qrscanner.remote.PosService


class RetrofitUtil {
    companion object{
        val posService : PosService = ApplicationClass.retrofit.create(PosService::class.java)
    }
}