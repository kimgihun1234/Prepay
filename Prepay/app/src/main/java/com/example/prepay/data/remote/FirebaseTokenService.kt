package com.example.prepay.data.remote
import com.example.prepay.data.response.TokenReq
import retrofit2.Call
import retrofit2.http.*

interface FirebaseTokenService {
    // Token정보 서버로 전송
    @POST("user/setFcmToken")
    fun uploadToken(@Header("email") email: String, @Body request : TokenReq): Call<String>
    @POST("broadcast")
    fun broadcast( @Query("title") title: String, @Query("body") body: String ): Call<String>

}