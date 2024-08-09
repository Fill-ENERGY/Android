package com.example.energy.data.repository.auth

import com.example.energy.data.model.UserModel

//카카오&일반 로그인
data class AuthResponse(
    val code: String,
    val message: String,
    val result: UserModel
)

//일반 회원가입
data class AuthSignUpResponse(
    val code: String,
    val message: String,
    val result: SignUpResult
)

data class SignUpResult(
    var id: Int?,
    var createdAt: String?
)

//토큰 재발급
data class RefreshTokenResponse(
    var accessToken: String?,
    var refreshToken: String?,
)