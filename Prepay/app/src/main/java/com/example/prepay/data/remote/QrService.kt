package com.example.prepay.data.remote

import com.example.prepay.data.response.QrRes
import com.example.prepay.data.response.SignInTeamReq
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface QrService {
    //개인용 qr생성
    @GET("/qr/private")
    suspend fun qrPrivateCreate(@Header("userEmail") userEmail: String): QrRes
    @GET("/qr/{teamId}/party")
    suspend fun qrTeamCreate(@Header("userEmail") userEmail: String,@Path("teamId") teamId: Int) :QrRes

}