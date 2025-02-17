package com.example.prepay.data.remote

import com.example.prepay.data.response.StoreAllRes
import com.example.prepay.data.response.StoreRes
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreService {

    // 프라이빗 추가
    @GET("store/stores/{teamId}")
    suspend fun getStores(@Header("access") access: String, @Query("teamId") teamId :Long) : List<StoreRes>

    // 퍼블릭 팀생성
    @GET("store/stores/all")
    suspend fun getAllStores(@Header("access") access: String) : List<StoreAllRes>
}