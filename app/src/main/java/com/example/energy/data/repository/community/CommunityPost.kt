package com.example.energy.data.repository.community

import android.net.Uri

data class CommunityPost(
    val userProfile: Int, //프로필 사진
    val userName: String, //사용자 이름
    val title: String, //제목
    val content: String, //내용
    val category: List<String>, //카테고리 리스트
    val imageUrl: List<Uri>, //사진 리스트
    val likes: String, //좋아요 수
    val comments: String, //댓글 수
)
