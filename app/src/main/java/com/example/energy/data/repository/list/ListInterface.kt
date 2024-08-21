package com.example.energy.data.repository.list



import com.example.energy.data.repository.map.ListResponse
import com.example.energy.data.repository.map.StationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ListInterface {


    //충전소 전체 가져오는 API
    @GET("/api/v1/stations")
    fun getListStation(
        @Header("Authorization") accessToken: String,
        @Query("query") query: String,
        @Query("lastId") lastId: Int,
        @Query("offset") offset: Int?,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<ListResponses>



    //충전소 개별 조회
    @GET("/api/v1/stations/{stationId}")
    fun getStation(
        @Header("Authorization") accessToken: String,
        @Path("stationId") stationId: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Call<StationResponse>

}