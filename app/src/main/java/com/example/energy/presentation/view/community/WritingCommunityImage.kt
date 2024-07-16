package com.example.energy.presentation.view.community

import android.net.Uri

data class WritingCommunityImage(
    val imageUrl: Uri, // 선택한 이미지
    var isRepresentative: Boolean = false, // 대표 이미지 여부를 나타내는 속성 추가
)
