package com.example.energy.data.repository.auth

data class AuthSignUpBody(
    var name : String = "힘전",
    var email : String = "test@umc.com",
    var password : String = "test1234!!",
    var passwordCheck : String = "test1234!!"
)
