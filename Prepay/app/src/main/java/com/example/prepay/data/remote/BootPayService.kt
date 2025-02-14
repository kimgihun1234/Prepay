package com.example.prepay.data.remote

import com.example.prepay.data.response.BootPayChargeReq
import com.example.prepay.data.response.BootPayChargeRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BootPayService {

    @POST("bootpay-charge")
    suspend fun getBootPay(@Header("access") access: String,
                           @Body request: BootPayChargeReq
    ): Response<BootPayChargeRes>

}