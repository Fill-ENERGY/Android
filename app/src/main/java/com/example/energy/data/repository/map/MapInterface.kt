package com.example.energy.data.repository.map

import ResultSearchKeyword
import com.example.energy.data.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MapInterface {
    //지도 검색 api
    @GET("v2/local/search/keyword.json") // Keyword.json의 정보를 받아옴
    fun getSearchKeyword(
        @Header("Authorization") key: String, // 카카오 API 인증키 [필수]
        @Query("query") query: String // 검색을 원하는 질의어 [필수]
    ): Call<ResultSearchKeyword> // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김

    //충전소 즐겨찾기
    @POST("/api/v1/stations/{stationId}/favorite")
    fun postBookmarkStation(
        @Header("Authorization") accessToken: String,
        @Path("stationId") stationId: Any,
    ): Call<BaseResponse>

    //충전소 전체 가져오는 API
    @GET("/api/v1/stations")
    fun getListStation(
        @Header("Authorization") accessToken: String,
        @Query("query") query: String,
        @Query("lastId") lastId: Int,
        @Query("offset") offset: Int?,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<ListResponse>

    //충전소 개별 조회
    @GET("/api/v1/stations/{stationId}")
    fun getStation(
        @Header("Authorization") accessToken: String,
        @Path("stationId") stationId: Any,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<StationResponse>

    //즐겨찾기한 충전소 조회
    @GET("/api/v1/stations/my-favorites")
    fun getBookmarkStation(
        @Header("Authorization") accessToken: String,
        @Query("query") query: String,
        @Query("lastId") lastId: Int,
        @Query("offset") offset: Int?,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<StationBookmarkResponse>


    //지도에 표시할 모든 충전소 정보 조회
    @GET("/api/v1/stations/location")
    fun getAllStation(
        @Header("Authorization") accessToken: String,
    ): Call<MapResponse>
}