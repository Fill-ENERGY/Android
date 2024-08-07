package com.example.energy.data.model

import java.time.LocalDateTime

data class UserModel(
    val userId: Int?,
    val createdAt: String?,
    val accessToken: String?,
    val refreshToken: String?,
)
