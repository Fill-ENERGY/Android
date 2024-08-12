package com.example.energy.data.repository.note

import android.security.identity.ResultData

data class LeaveChatResponse(
    val code: String,
    val message: String,
    val result: ResultData
)


data class ResultData(
    val threadId: Long,
    val memberId: Long,
    val status: String, // ACTIVE or LEFT
    val leftAt: String // LocalDateTime as a String
)
