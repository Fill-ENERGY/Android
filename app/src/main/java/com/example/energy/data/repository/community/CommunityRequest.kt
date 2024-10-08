package com.example.energy.data.repository.community

import okhttp3.MultipartBody
import retrofit2.http.Multipart

// 게시글 작성 요청 데이터 / 수정 요청 데이터
data class PostBoardRequest(
    val title: String,
    val content: String,
    val category: String,
    val images: List<String>,
)



// 이미지 업로드 요청
data class UploadImagesRequest(
    val images: List<MultipartBody.Part>,
)

// 댓글 작성 요청
data class WriteCommentRequest(
    val content: String,
    val secret: Boolean,
    val parentCommentId: Int? = null,
    val images: List<String>,
)
// 댓글 수정 요청
data class UpdateCommentRequest(
    val content: String,
    val images: List<String>,
    val secret: Boolean,
)


// 도와줘요 상태 변경 요청
data class HelpStatusRequest(
    val helpStatus: String,
)