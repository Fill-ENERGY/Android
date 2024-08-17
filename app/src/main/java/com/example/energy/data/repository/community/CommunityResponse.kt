package com.example.energy.data.repository.community

import android.text.Editable

// 커뮤니티 전체 가져오는 API
data class CommunityResponse (
    val code: String,
    val message: String,
    val result: CommunityModel,
)
data class CommunityModel (
    var boards: List<BoardModel>,
    var nextCursor: Int,
    var hasNext: Boolean,
)


// 게시글 관련 Response 데이터
data class BoardModel(
    var id: Int,
    var memberId: Int,
    var memberName: String,
    var title: String,
    var content: String,
    var category: String,
    var helpStatus: String,
    var isAuthor: Boolean,
    var likeNum: Int,
    var commentCount: Int,
    var createdAt: String,
    var updatedAt: String,
    var images: List<String>,
)


// 게시글 이미지 업로드
data class UploadImagesResponse(
    val code: String,
    val message: String,
    val result: ImagesModel,
)
data class ImagesModel (
    var images: List<String>
)


// 게시글 작성
data class UploadResponse (
    val code: String,
    val message: String,
    val result: BoardModel
)


// 게시글 삭제
data class DeleteCommunityResponse(
    val code: String,
    val message: String,
    val result: Int,
)


// 상세 게시글 조회
data class DetailResponse(
    val code: String,
    val message: String,
    val result: DetailModel
)
data class DetailModel(
    var board: BoardModel,
)

// 댓글 조회
data class CommentListResponse(
    val code: String,
    val message: String,
    val result: List<CommentModel>,
)

// 댓글 작성
data class CommentResponse(
    val code: String,
    val message: String,
    val result: CommentModel,
)
data class CommentModel(
    var id: Int,
    var content: String,
    var secret: Boolean,
    var memberId: Int,
    var memberName: String,
    var createdAt: String,
    var images: List<ImagesModel>,
    var deleted: Boolean,
    var parentId: Int,
    var author: Boolean,
    var canViewSecret: Boolean,
    var replies: List<String>,
)


// 좋아요 추가
data class LikeResponse(
    val code: String,
    val message: String,
    val result: LikeModel,
)
data class LikeModel(
    var memberId: Int,
    var boardId: Int,
    var likeCount: Int,
)