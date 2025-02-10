package com.example.prepay.data.remote

import com.example.prepay.data.response.QrRes
import com.example.prepay.data.response.SignInTeamReq
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface QrService {
    //개인용 qr생성
    @POST("/qr/private")
    suspend fun qrPrivateCreate(@Header("userEmail") userEmail: String): QrRes

}