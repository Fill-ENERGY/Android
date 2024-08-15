package com.example.energy.data.repository.community

// 게시글 작성 요청 데이터
data class PostBoardRequest(
    val title: String?,
    val content: String?,
    val category: String?,
    val images: List<String>?,
)


// 이미지 업로드 요청
data class UploadImagesRequest(
    val images: List<String>?,
)

// 댓글 작성 요청
data class WriteCommentRequest(
    val content: String?,
    val secret: Boolean?,
    val parentCommentId: Long?,
    val images: List<String>?,
)