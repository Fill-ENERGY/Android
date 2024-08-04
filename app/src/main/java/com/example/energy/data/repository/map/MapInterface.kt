package com.example.energy.data.repository.map

import ResultSearchKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MapInterface {
    //지도 검색 api
    @GET("v2/local/search/keyword.json") // Keyword.json의 정보를 받아옴
    fun getSearchKeyword(
        @Header("Authorization") key: String, // 카카오 API 인증키 [필수]
        @Query("query") query: String // 검색을 원하는 질의어 [필수]
    ): Call<ResultSearchKeyword> // 받아온 정보가 ResultSearchKeyword 클래스의 구조로 담김

    //모든 충전소 정보 조회
    @GET("/api/v1/stations/location")
    fun getAllStation(
        @Header("Authorization") key: String,
    ): Call<MapResponse>

    //충전소 개별 조회
    @GET("/api/v1/stations/{stationId}")
    fun getStation(
        @Header("Authorization") key: String,
        @Path("stationId") stationId: String,
    ): Call<MapResponse>
}