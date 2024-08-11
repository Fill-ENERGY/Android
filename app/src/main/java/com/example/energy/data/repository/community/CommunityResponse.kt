package com.example.energy.data.repository.community

// 커뮤니티 전체 가져오는 API
data class CommunityResponse (
    val code: String,
    val message: String,
    val result: CommunityModel,
)
data class CommunityModel (
    var board: List<BoardModel>,
    var next_cursor: Long?,
    var has_next: Boolean?,
)


// 게시글 관련 Response 데이터
data class BoardModel(
    var board_id: Long?,
    var member_id: Long?,
    var member_name: String?,
    var title: String?,
    var content: String?,
    var category: String?,
    var helpStatus: String?,
    var is_author: Boolean? = true,
    var created_at: String,
    var updated_at: String,
    var like_num: Int? = 0,
    var comment_count: Int? = 0,
    var images: List<ImagesResponse>?,
)


// 게시글 이미지
data class ImagesResponse (
    var board_img_id: String?,
    var img_uri: String?,
)


// 게시글 작성
data class UploadResponse (
    val code: String?,
    val message: String?,
    val result: BoardModel?,
)


// 상세 게시글 조회
data class DetailResponse(
    val code: String?,
    val message: String?,
    val result: DetailModel,
)
data class DetailModel(
    var board: BoardModel,
    var comment: List<CommentModel>,
)

// 댓글
data class CommentModel(
    var comment_id: Long?,
    var content: String?,
    var isSecret: Boolean?,
    var memberId: Long?,
    var memberName: String?,
    var createdAt: String?,
    var images: List<String>?,
    var secret: Boolean,
)


// 좋아요 추가
data class LikeResponse(
    val code: String?,
    val message: String?,
    val result: LikeModel?,
)
data class LikeModel(
    var member_id: Long?,
    var board_id: Long?,
    var like_count: Int?,
)