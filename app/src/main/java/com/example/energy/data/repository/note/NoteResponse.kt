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

//채팅방 목록 조회
data class ChatThreadsResponse (
    val code: String?,
    val message: String?,
    val result: ChatThreadsResult?
)

data class ChatThreadsResult(
    val threads: List<ChatThread>?,
    val cursor: String?,
    val lastId: Int?,
    val hasNext: Boolean
)

data class ChatThread(
    val threadId: Int?,
    val receiverId: Int?,
    val name: String?,
    val nickname: String?,
    val profileImg: String?,
    val recentMessage: RecentMessage?,
    val unreadMessageCount: Int?,
    val updatedAt: String?
)

data class RecentMessage(
    val messageId: Int?,
    val content: String?,
    val createdAt: String?
)


//쪽지목록 조회
data class GetMessageResponse(
    val code: String?,
    val message: String?,
    val result: GetMessageResult?
)

data class GetMessageResult(
    val threadId: Int?,
    val receiverId: Int?,
    val name: String?,
    val nickname: String?,
    val profileImg: String?,
    val messages: List<GetDetailMessage>?,
    val nextCursor: Int?,
    val hasNext: Boolean?
)


data class GetDetailMessage(
    val messageId: Int?,
    val threadId: Int?,
    val content: String?,
    val images: List<String>?,
    val sender: Int?,
    val receiver: Int?,
    val readStatus: String?,
    val createdAt: String?

)

// 채팅방 나가기
data class LeaveChatRoomResponse(
    val code: String?,
    val message: String?,
    val result: LeaveChatResult?
)

data class LeaveChatResult(
    val threadId: Int?,
    val memberId: Int?,
    val status: String,
    val leftAt: String?
)


// 커뮤니티 -> 채팅방 목록 조회
data class CommunityGetMessagesResponse(
    val code: String?,
    val message: String?,
    val result: GetThreadId?
)

data class GetThreadId(
    val threadId: Int?
)



