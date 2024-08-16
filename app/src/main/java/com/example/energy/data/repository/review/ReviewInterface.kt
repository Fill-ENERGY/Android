package com.example.energy.data.repository.review

import com.example.energy.data.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewInterface {
    //평가 생성 API
    @POST("/api/v1/reviews")
    fun postReview(
        @Header("Authorization") accessToken: String,
        @Body postReviewBody : PostReviewBody
    ): Call<PostReviewResponse>

    //평가 하나의 정보 가져오는 API
    @GET("/api/v1/reviews/{reviewId}")
    fun getReviewInfo(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
    ): Call<ReviewResponse>

    //평가 추천 API
    @POST("/api/v1/reviews/{reviewId}")
    fun recommendReview(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
    ): Call<RecommendReviewResponse>

    //평가 삭제 API
    @DELETE("/api/v1/reviews/{reviewId}")
    fun deleteReview(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
    ): Call<BaseResponse>

    //평가 수정 API
    @PATCH("/api/v1/reviews/{reviewId}")
    fun editReview(
        @Header("Authorization") accessToken: String,
        @Path("reviewId") reviewId: Int,
        @Body patchReviewBody : PatchReviewBody
    ): Call<ReviewResponse>

    //이미지 업로드
    @Multipart
    @POST("/api/v1/reviews/images")
    fun postImages(
        @Header("Authorization") accessToken: String,
        @Part images : List<MultipartBody.Part>?
    ): Call<UploadImageResponse>

    //본인 평가 목록 가져오는 API
    @GET("/api/v1/reviews/members")
    fun getMyReviews(
        @Header("Authorization") accessToken: String,
    ): Call<AllReviewResponse>


    //특정 충전소의 모든 평가를 정렬하는 API
    @GET("/api/v1/reviews/stations/{stationId}")
    fun getReviewsStation(
        @Header("Authorization") accessToken: String,
        @Path("stationId") stationId: Any,
        @Query("lastId") lastId: Int,
        @Query("query") query: String,
        @Query("offset") offset: Int,
    ): Call<AllReviewResponse>

    //평가의 키워드 가져오는 API
    @GET("/api/v1/reviews/keywords")
    fun getReviewKeywords(
        @Header("Authorization") accessToken: String,
    ): Call<AllKeywordResponse>

}