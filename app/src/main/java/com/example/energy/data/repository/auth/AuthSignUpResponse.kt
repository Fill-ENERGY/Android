package com.example.energy.data.repository.auth

import com.example.energy.data.model.UserModel

data class AuthSignUpResponse(
    val code: String,
    val message: String,
    val result: SignUpResult
)

data class SignUpResult(
    var id: Int?,
    var createdAt: String?
)
