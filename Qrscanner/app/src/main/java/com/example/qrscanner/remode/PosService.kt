package com.example.qrscanner.remode

import com.example.qrscanner.response.PosReq
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PosService {
    @POST("/pos/order")
    suspend fun posTransfer(@Body request: PosReq): Response<Int>
}