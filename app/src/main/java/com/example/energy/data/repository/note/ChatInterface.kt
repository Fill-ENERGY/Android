package com.example.energy.data.repository.note

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatInterface {




    @Headers("Content-Type: application/json")
    @POST("/api/v1/messages")
    fun sendMessages(

        @Header("Authorization") accessToken: String,
        @Body request: MessageRequest?

    ) :Call<MessageResponse>


    @GET("/api/v1/threads")
    fun getChatThreads(

        @Header("Authorization") accessToken: String,
        @Query("cursor") cursor: String?,
        @Query("lastId") lastId: Int?,
        @Query("limit") limit: Int?
    ) :Call<ChatThreadsResponse>


    @GET("/api/v1/threads/{threadId}/messages")
    fun getMessages (
        @Header("Authorization") accessToken: String,
        @Path("threadId") threadId: Int?,
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ) :Call<GetMessageResponse>



    @PATCH("/api/v1/threads/{threadId}/messages")
    fun updateReadMessage(
        @Header("Authorization") accessToken: String,
        @Path("threadId") threadId: Int?,
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ) : Call<GetMessageResponse>



    @PATCH("api/v1/threads/{threadId}")

    fun leaveChatRoom(
        @Header("Authorization") accessToken: String,
        @Path("threadId") threadId: Int?
    ): Call<LeaveChatRoomResponse>



    @GET("/api/v1/threads/members/{memberId}")

    fun communityGetMessages(
        @Header("Authorization") accessToken: String,
        @Path("memberId") memberId: Int?,
    ): Call<CommunityGetMessagesResponse>




}