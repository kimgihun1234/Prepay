package com.example.prepay.data.remote

import com.example.prepay.data.model.dto.BootPayCharge
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BootPayService {

    @POST("bootpay-charge")
    suspend fun getBootPay(@Header ("email") email: String,
                           @Body request: BootPayCharge): Response<BootPayCharge>

}