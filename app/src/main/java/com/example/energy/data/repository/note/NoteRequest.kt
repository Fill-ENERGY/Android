package com.example.energy.data.repository.note

data class MessageRequest(

    val threadId: Int?,
    val content: String?,
    val images: List<String>?,
    val receiverId: Int?

)
