package com.example.energy.data.model

import java.time.LocalDateTime

data class UserModel(
    val id: Long?,
    val createdAt: LocalDateTime?,
    val accessToken: String?,
    val refreshToken: String?,
)
