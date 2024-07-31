package com.example.energy.data.repository.auth

import com.example.energy.data.model.UserModel

data class AuthResponse(
    val code: String,
    val message: String,
    val result: UserModel
)
