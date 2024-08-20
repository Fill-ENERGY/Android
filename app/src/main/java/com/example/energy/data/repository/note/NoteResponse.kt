package com.example.energy.data.repository.note

data class MessageResponse(
    val code: String?,
    val message: String?,
    val result: MessageResult
)


data class MessageResult (
    val messageId: Int?,
    val threadId: Int?,
    val content: String?,
    val images: List<String>,
    val sender: Int?,
    val receiver: Int?,
    val readStatus: String?,
    val createdAt: String
)