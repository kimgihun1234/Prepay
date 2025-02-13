package com.example.prepay.data.remote

import com.example.prepay.data.response.LoginResponse
import com.example.prepay.response.LoginRequest
import com.example.prepay.response.SignupRequest
import com.example.prepay.response.SignupResponse
import com.example.prepay.response.KakaoLoginRequest
import com.example.prepay.response.KakaoLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @POST("user/signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("user/login")
    suspend fun login(@Body request: LoginRequest) :Response<LoginResponse>

    @GET("/user/kakao/login/{code}")
    suspend fun kakaoLogin(
        @Path("code") code: String,
    ): Response<KakaoLoginResponse>




}