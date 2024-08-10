package com.example.energy.data.repository.block

import com.example.energy.data.BaseResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BlockInterface {
    //차단 API
    @POST("/api/v1/blocks/members/{targetMemberId}")
    fun postBlockMember(
        @Header("Authorization") accessToken: String,
        @Path("targetMemberId") targetMemberId: Int
        ): Call<BlockResponse>

    //차단 멤버 조회 API
    @GET("/api/v1/blocks/members")
    fun getBlockMembers(
        @Header("Authorization") accessToken: String,
        @Query("cursor") cursor: Int,
        @Query("limit") limit: Int
    ): Call<BlockListResponse>

    @DELETE("/api/v1/blocks/{blockId}")
    fun deleteBlockMember(
        @Header("Authorization") accessToken: String,
        @Path("blockId") blockId: Int
    ): Call<BaseResponse>
}