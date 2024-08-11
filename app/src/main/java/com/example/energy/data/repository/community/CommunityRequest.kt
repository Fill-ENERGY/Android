package com.example.energy.data.repository.community

// 게시글 작성 요청 데이터
data class PostBoardRequest(
    val title: String?,
    val content: String?,
    val category: String?,
    val images: List<String>?,
)