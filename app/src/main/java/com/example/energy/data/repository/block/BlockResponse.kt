package com.example.energy.data.repository.block

//차단 API
data class BlockResponse(
    val code: String,
    val message: String,
    val result: BlockUserModel
)

data class BlockUserModel (
    var blockId: Int?,
    var memberId: Int?,
    var targetMemberId: Int?,
    var name: String?,
    var email: String?,
    var profileImg: String?,
)

//차단 멤버 조회 API
data class BlockListResponse(
    val code: String,
    val message: String,
    val result: BlockListResult
)

data class BlockListResult (
    var blocks: List<BlockUserModel>?,
    var nextCursor: Int?,
    var hasNext: Boolean?
)