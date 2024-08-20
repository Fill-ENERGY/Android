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
    var likeNum: Int,
    var commentCount: Int,
    var createdAt: String,
    var updatedAt: String,
    var images: List<String>,
    var liked: Boolean,
    var author: Boolean,
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
    val result: BoardModel
)

// 댓글 조회
data class CommentListResponse(
    val code: String,
    val message: String,
    val result: CommentResult,
)

// 댓글 리스트를 감싸는 클래스
data class CommentResult(
    val comments: List<CommentModel>
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
    var memberId: Int,
    var memberName: String,
    var createdAt: String,
    var images: List<String>,
    var parentId: Int,
    var author: Boolean,
    var replies: List<CommentModel>,
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
    var liked: Boolean,
)


// 도와줘요 상태 변경
data class HelpStatusResponse(
    val code: String,
    val message: String,
    val result: HelpStatusModel,
)
data class HelpStatusModel(
    var boardId: Int,
    var helpStatus: String,
)
