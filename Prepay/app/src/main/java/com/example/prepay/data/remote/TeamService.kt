package com.example.prepay.data.remote

import com.example.prepay.data.model.dto.PublicPrivateTeam
import com.example.prepay.data.response.BanUserReq
import com.example.prepay.data.response.GetUserOfTeamRes
import com.example.prepay.data.response.LikeTeamsReq
import com.example.prepay.data.response.MoneyChangeReq
import com.example.prepay.data.response.PrivilegeUserReq
import com.example.prepay.data.response.PublicLikeRes
import com.example.prepay.data.response.PublicTeamDetailsRes
import com.example.prepay.data.response.PublicTeamsDisRes
import com.example.prepay.data.response.PublicTeamsRes
import com.example.prepay.data.response.SignInTeamReq
import com.example.prepay.data.response.StoreLocation
import com.example.prepay.data.response.Team
import com.example.prepay.data.response.TeamDetailRes
import com.example.prepay.data.response.TeamIdReq
import com.example.prepay.data.response.TeamIdRes
import com.example.prepay.data.response.TeamIdStoreRes
import com.example.prepay.data.response.TeamStoreReq
import com.example.prepay.data.response.TeamStoreRes
import com.example.prepay.data.response.TeamUserRes
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TeamService {

    //팀 가맹점 추가
    @Multipart
    @POST("team/store")
    suspend fun createStore(@Header("access") access: String,
                            @Part("request") request: TeamStoreReq,
                            @Part image: MultipartBody.Part?): Response<TeamStoreRes>
    //팀을 생성하는 과정
    @Multipart
    @POST("team/signup")
    suspend fun makeTeam(@Header("access") access: String,
                         @Part("request") request: PublicPrivateTeam,
                         @Part image: MultipartBody.Part?) : Response<TeamIdRes>

    //팀 가맹점 추가
    @POST("/team/signin")
    suspend fun signInTeam(@Header("access") access: String, @Body request: SignInTeamReq): GetUserOfTeamRes

    // 본인의 전체 팀정보 조회
    @GET("/team/myTeams")
    suspend fun getTeamList(@Header("access") access: String): List<Team>
    // 본인의 팀의 상세 정보 조회
    @GET("/team/{teamId}")
    suspend fun getTeamDetails(@Header("access") access: String, @Path("teamId") teamId: Long): TeamDetailRes

    //한도 변경
    @POST("/team/limit")
    suspend fun moneyChange(@Header("access") access: String, @Body request: MoneyChangeReq) : TeamDetailRes


    //특정 사용자를 팀에서 강퇴합니다.
    @POST("/team/ban")
    suspend fun banUser(@Header("access") access: String, @Body request: BanUserReq): Map<String,String>

    //특정 사용자를 회식권한을 줍니다.
    @POST("/team/privilege")
    suspend fun privilegeUser(@Header("access") access: String, @Body request: PrivilegeUserReq): Map<String,String>

    //사용자가 팀을 탈퇴합니다.
    @POST("/team/exit")
    suspend fun exitTeam(@Header("access") access: String, @Body request: TeamIdReq): Map<String, String>

    //특정 팀의 사용자 목록을 가져옵니다.
    @GET("/team/{teamId}/user")
    suspend fun getUserOfTeam(@Header("access") access: String, @Path("teamId") teamId: Long): List<TeamUserRes>

    //특정 팀의 식당 목록을 가져옵니다.
    @GET("/team/{teamId}/stores")
    suspend fun getStoreOfTeam(@Header("access") access: String, @Path("teamId") teamId: Long): List<TeamIdStoreRes>

    //공개된 모든 팀 목록을 가져옵니다.

    @GET("/team/public-teams")
    suspend fun getPublicTeams(@Header("access") access: String, @Query("latitude") latitude : Double, @Query("longitude") longitude:Double): List<PublicTeamsRes>


    //특정 키워드로 공개된 팀을 검색합니다.
    @GET("/team/public-teams/{keyword}")
    suspend fun getPublicTeamsByKeyword(@Path("keyword") keyword: String): List<PublicTeamsRes>

    //식당의 위치를 가져옵니다.
    @GET("/team/coordinate/{teamId}")
    suspend fun getTeamStore(@Header("access") access: String, @Path("teamId") teamId: Long) : List<StoreLocation>

    //공개된 그룹의 좋아요 정보를 보냅니다.



    suspend fun getTeamStoreDistance(@Header("access") access : String, @Query("latitude") latitude : Double, @Query("longitude") longitude:Double) : List<PublicTeamsDisRes>

    @POST("/team/like")
    suspend fun sendLikeStatus(@Header("access") access: String, @Body request: LikeTeamsReq): Map<String, Int>

    @GET("/team/public-team/{teamid}")
    suspend fun groupDetailInfo(@Header("access") access: String, @Path("teamid") teamid: Long): PublicTeamDetailsRes

    @GET("/team/public/liked")
    suspend fun getlikeTeamList(@Header("access") access: String,@Query("latitude") latitude : Double, @Query("longitude") longitude:Double): List<PublicLikeRes>
    
}