package com.example.energy.data.repository.community

import com.example.energy.data.repository.map.ListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityInterface {

    //전체 커뮤니티 조회 api
    @GET("/api/v1/boards")
    fun getListCommunity(
        @Header("Authorization") accessToken: String,
        @Query("category") category: String,
        @Query("cursor") cursor: Long? = 0,
        @Query("limit") limit: Int? = 10,
        @Query("sort") sort: String? = "latest",
    ): Call<CommunityResponse>


    //개별 상세 커뮤니티 조회 api
    @GET("/api/v1/boards/{boardId}")
    fun getDetailCommunity(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Long,
    ): Call<DetailResponse>


    //게시글 작성 api
    @POST("/api/v1/boards")
    fun postBoard(
        @Header("Authorization") accessToken: String,
        @Body request: PostBoardRequest,
    ): Call<UploadResponse>


    //게시글 좋아요 추가 api
    @POST("/api/v1/boards/{boardId}/likes")
    fun postLikeBoard(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Long,
    ): Call<LikeResponse>
}