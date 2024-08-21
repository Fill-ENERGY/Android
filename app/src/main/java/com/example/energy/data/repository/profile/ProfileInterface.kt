package com.example.energy.data.repository.profile

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileInterface {

    //내 프로필 조회
    @GET("/api/v1/profiles")
    fun getMyProfile(
        @Header("Authorization") accessToken: String,
    ): Call<ProfileResponse>
}