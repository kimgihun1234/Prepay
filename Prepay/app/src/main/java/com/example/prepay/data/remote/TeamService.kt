package com.example.prepay.data.remote

import com.example.prepay.data.model.dto.PublicPrivateTeam
import com.example.prepay.data.response.Team
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
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
    // 전체 팀정보 조회
    @GET("/team/myTeams")
    suspend fun getTeamList(@Header("userId")  userId: Long): List<Team>

}