package com.example.prepay.data.remote
import retrofit2.Call
import retrofit2.http.*

interface FirebaseTokenService {
    // Token정보 서버로 전송
    @POST("token")
    fun uploadToken(@Query("token") token: String): Call<String>
    @POST("broadcast")
    fun broadcast( @Query("title") title: String, @Query("body") body: String ): Call<String>

}