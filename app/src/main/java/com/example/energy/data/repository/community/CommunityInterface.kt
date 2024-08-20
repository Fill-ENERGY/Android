package com.example.energy.data.repository.community

import com.example.energy.data.repository.map.ListResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityInterface {

    //전체 커뮤니티 조회 api
    @GET("/api/v1/boards")
    fun getListCommunity(
        @Header("Authorization") accessToken: String,
        @Query("category") category: String,
        @Query("cursor") cursor: Int? = 0,
        @Query("limit") limit: Int? = 10,
        @Query("sort") sort: String? = "LATEST",
    ): Call<CommunityResponse>


    //개별 상세 커뮤니티 조회 api
    @GET("/api/v1/boards/{boardId}")
    fun getDetailCommunity(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
    ): Call<DetailResponse>


    //게시글 작성 api
    @POST("/api/v1/boards")
    fun postBoard(
        @Header("Authorization") accessToken: String,
        @Body request: PostBoardRequest,
    ): Call<UploadResponse>


    //게시글 수정 api
    @PUT("/api/v1/boards/{boardId}")
    fun updateBoard(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
        @Body request: PostBoardRequest,
    ): Call<UploadResponse>


    //게시글 삭제 api
    @DELETE("/api/v1/boards/{boardId}")
    fun deleteBoard(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
    ): Call<DeleteCommunityResponse>


    //이미지 업로드 api
    @Multipart
    @POST("/api/v1/boards/images")
    fun uploadImages(
        @Header("Authorization") accessToken: String,
        @Part("images") images: UploadImagesRequest
    ): Call<UploadImagesResponse>


    //게시글 좋아요 추가 api
    @POST("/api/v1/boards/{boardId}/likes")
    fun postLikeBoard(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
    ): Call<LikeResponse>
    //게시글 좋아요 삭제 api
    @DELETE("/api/v1/boards/{boardId}/likes")
    fun deleteLikeBoard(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
    ): Call<LikeResponse>

    //댓글 조회 api
    @GET("/api/v1/boards/{boardId}/comments")
    fun getListComment(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
    ): Call<CommentListResponse>

    //댓글 작성 api
    @POST("/api/v1/boards/{boardId}/comments")
    fun writeCommentBoard(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
        @Body request: WriteCommentRequest,
    ): Call<CommentResponse>

    //댓글 수정 api
    @PUT("/api/v1/boards/{boardId}/comments/{commentId}")
    fun updateComment(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
        @Path("commentId") commentId: Int,
        @Body request: UpdateCommentRequest,
    ): Call<CommentResponse>

    //댓글 삭제 api
    @DELETE("/api/v1/boards/{boardId}/comments/{commentId}")
    fun deleteComment(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
        @Path("commentId") commentId: Int,
    ): Call<DeleteCommentResponse>


    //도와줘요 상태 변경 api
    @PATCH("/api/v1/boards/{boardId}/status")
    fun helpStatus(
        @Header("Authorization") accessToken: String,
        @Path("boardId") boardId: Int,
        @Body request: HelpStatusRequest
    ): Call<HelpStatusResponse>
}