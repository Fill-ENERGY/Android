package com.example.energy.data.repository.review


//평가 생성 API
data class PostReviewResponse(
    val code: String,
    val message: String,
    val result: PostReviewResult
)

data class PostReviewResult(
    var id: Int,
    var createdAt: String?
)

//평가 하나의 정보 가져오는 API, 평가 수정 API
data class ReviewResponse(
    val code: String,
    val message: String,
    val result: ReviewModel
)

//평가 추천, 삭제
data class RecommendReviewResponse(
    val code: String,
    val message: String,
    val result: RecommendReviewModel
)

//이미지 업로드
data class UploadImageResponse(
    val code: String,
    val message: String,
    val result: ImageModel?
)

//본인 평가 목록 가져오는 API, 특정 충전소의 모든 평가를 정렬하는 API
data class AllReviewResponse(
    val code: String,
    val message: String,
    val result: AllReviewResult,
)

data class AllReviewResult(
    val reviews: List<ReviewModel>?,
    val hasNext: Boolean,
    val lastId: Int?,
)

//평가의 키워드 가져오는 API
data class AllKeywordResponse(
    val code: String,
    val message: String,
    val result: List<KeywordModel>?
)


//모델 관련
data class ImageModel(
    var images: List<String>?
)

data class ReviewModel(
    var id: Int,
    var content: String,
    var recommendationNum: Int,
    var keywords: List<KeywordModel>?,
    var images: List<String>?,
    var score: Double,
    var authorId: Int?,
    var authorName: String?,
    val username: String?,
    var recommended: Boolean,
)

data class RecommendReviewModel(
    var reviewId: Int?,
    var memberId: Int?,
    var recommendCount: Int?,
)

data class KeywordModel(
    var name: String?,
    var content: String?,
)

data class ProfileModel(
    var score: Double?,
    var authorId: Int?,
    var authorName: String?,
    val username: String?,
    var recommended: Boolean,
    )
