package com.example.prepay.data.remote

import com.example.prepay.data.model.dto.PublicPrivateTeam
import com.example.prepay.data.response.BanUserReq
import com.example.prepay.data.response.GetUserOfTeamRes
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.data.response.Team
import com.example.prepay.data.response.TeamDetailRes
import com.example.prepay.data.response.TeamIdReq
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface TeamService {

    //팀을 생성하는 과정
    @Multipart
    @POST("team/signup")
    suspend fun makeTeam(@Header("userId") userId: String,
                         @Part("request") request: PublicPrivateTeam,
                         @Part image: MultipartBody.Part?) : Response<PublicPrivateTeam>
    // 본인의 전체 팀정보 조회
    @GET("/team/myTeams")
    suspend fun getTeamList(@Header("userId")  userId: Long): List<Team>
    // 본인의 팀의 상세 정보 조회
    @GET("/team/{teamId}")
    suspend fun getTeamDetails(@Header("userId") userId: Long, @Path("teamId") teamId: Long): TeamDetailRes

    //특정 사용자를 팀에서 강퇴합니다.
    @POST("/team/ban")
    suspend fun banUser(@Header("userId") userId: Long, @Body request: BanUserReq): Map<String, String>

    //사용자가 팀을 탈퇴합니다.
    @POST("/team/exit")
    suspend fun exitTeam(@Header("userId") userId: Long, @Body request: TeamIdReq): Map<String, String>

    //특정 팀의 사용자 목록을 가져옵니다.
    @GET("/team/{teamId}/user")
    suspend fun getUserOfTeam(@Header("userId") userId: Long, @Path("teamId") teamId: Long): List<GetUserOfTeamRes>

    //공개된 모든 팀 목록을 가져옵니다.
    @GET("/team/public-teams")
    suspend fun getPublicTeams(): List<PublicTeamsRes>

    //특정 키워드로 공개된 팀을 검색합니다.
    @GET("/team/public-teams/{keyword}")
    suspend fun getPublicTeamsByKeyword(@Path("keyword") keyword: String): List<PublicTeamsRes>
}