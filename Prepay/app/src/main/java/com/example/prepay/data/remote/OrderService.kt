package com.example.prepay.data.remote

import com.example.prepay.data.model.dto.Receipt
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface OrderService {

    @GET("order/history/{detailHistoryId}")
    suspend fun getDetailReceipt(@Path("detailHistoryId") detailHistoryId : Int,
                                 @Header("userId") userId: Int) : List<Receipt>
}