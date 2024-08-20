package com.example.energy.data.repository.auth

import com.example.energy.data.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthInterface {
    //카카오 로그인
    @POST("/api/v1/members/social/oauth/kakao")
    fun kakaoLogin(
        @Header("Authorization") accessToken: String,
    ): Call<AuthResponse>

    //일반 회원가입
    @POST("/api/v1/members/signup")
    fun customSignUp(
        @Body authSignUpBody : AuthSignUpBody
    ): Call<AuthSignUpResponse>

    //로그아웃
    @POST("/api/v1/members/logout")
    fun logout(
        @Header("Authorization") accessToken: String,
    ): Call<BaseResponse>

    //토큰 재발급
    @GET("/api/v1/members/reissue")
    fun refreshToken(
        @Header("Authorization") accessToken: String,
        @Header("RefreshToken") refreshToken: String,
    ): Call<RefreshTokenResponse>
}