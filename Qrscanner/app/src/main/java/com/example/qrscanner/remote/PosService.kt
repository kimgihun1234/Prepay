package com.example.qrscanner.remote

import com.example.qrscanner.response.PosReq
import com.example.qrscanner.response.StoreReq
import com.example.qrscanner.response.StoreRes
import com.example.qrscanner.response.TeamRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PosService {
    @POST("/pos/order")
    suspend fun posTransfer(@Header("access") access : String, @Body request: PosReq): Response<Int>

    @POST("/owner/teams")
    suspend fun getTeamList(@Header("storeId") storeId : Int) : List<TeamRes>

    @POST("/owner/history")
    suspend fun getStoreList(@Body request: StoreReq) : List<StoreRes>
}