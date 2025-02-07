package com.example.prepay.data.remote

import com.example.prepay.data.response.Team
import retrofit2.http.GET
import retrofit2.http.Header

interface TeamService {
    // 전체 팀정보 조회
    @GET("/team/myTeams")
    suspend fun getTeamList(@Header("userId")  userId: Long): List<Team>
}

