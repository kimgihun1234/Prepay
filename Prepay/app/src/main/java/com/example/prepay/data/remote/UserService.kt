package com.example.prepay.data.remote

import com.example.prepay.response.LoginRequest
import com.example.prepay.response.LoginResponse
import com.example.prepay.response.SignupRequest
import com.example.prepay.response.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("user/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("user/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

}