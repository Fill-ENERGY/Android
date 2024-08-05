package com.example.energy.data.repository.auth

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthInterface {
    //카카오 로그인
    @POST("/")
    fun kakaoLogin(
        @Header("Authorization") accessToken: String,
    ): Call<AuthResponse>

    //회원탈퇴
    @DELETE("/")
    fun withdraw(
    ): Call<AuthResponse>

    //일반 회원가입
    @POST("/api/v1/members/signup")
    fun customSignUp(
        @Body authSignUpBody : AuthSignUpBody
    ): Call<AuthSignUpResponse>

}