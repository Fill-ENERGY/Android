package com.example.energy.data.repository.note

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ChatInterface {

    @PATCH("api/v1/threads/{threadId}")

    fun leaveChatRoom(
        @Header("Authorization") accessToken: String,
        @Path("threadId") threadId: Int
    ): Call<LeaveChatResponse>

}