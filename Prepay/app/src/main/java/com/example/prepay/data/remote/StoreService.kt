package com.example.prepay.data.remote

import com.example.prepay.data.response.StoreIdReq
import com.example.prepay.data.response.StoreIdRes
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface StoreService {

    @POST("store/stores")
    suspend fun getStores(@Body request : StoreIdReq,
                          @Header("email") email : String) : List<StoreIdRes>
}