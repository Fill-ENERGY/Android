package com.example.energy.data.repository.auth

//일반 회원가입
data class AuthSignUpBody(
    var name : String = "힘전",
    var email : String = "test@uccc.com",
    var password : String = "test1234!!",
    var passwordCheck : String = "test1234!!"
)

//일반 로그인
data class AuthLoginBody(
    var email: String = "test1234@umc.com",
    var password: String = "test1234!!"
)
