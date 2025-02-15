package com.example.prepay.data.remote

import com.example.prepay.data.model.dto.OrderHistory
import com.example.prepay.data.model.dto.Receipt
import com.example.prepay.data.response.OrderHistoryReq
import com.example.prepay.ui.RestaurantDetails.OrderHistoryAdapter
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderService {

    @POST("order/history")
    suspend fun getDetailHistory(@Header("access") access: String,
                                 @Body request: OrderHistoryReq,
                                 ) : List<OrderHistory>

    @GET("order/history/{detailHistoryId}")
    suspend fun getDetailReceipt(@Path("detailHistoryId") detailHistoryId : Int,
                                 @Header("access") access: String) : List<Receipt>
}