package com.example.prepay.data.remote

import com.example.prepay.data.model.dto.PublicPrivateTeam
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TeamService {

    @Multipart
    @POST("team/signup")
    suspend fun makeTeam(@Header("userId") userId: String,
                         @Part("request") request: PublicPrivateTeam,
                         @Part image: MultipartBody.Part?) : Response<PublicPrivateTeam>
}