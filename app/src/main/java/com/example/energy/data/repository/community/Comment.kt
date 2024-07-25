package com.example.energy.data.repository.community

import java.io.Serializable

data class Comment(
    val commentId: Int,
//    var reviewInfo: ReviewInComment,
    var userInfo: String, //UserInfo?,
    var body: String,
    var likeCount : Int = 0,
    val parentCommentId : Int?,
    var userLike : Boolean,
    var createTime: String,
    var updateTime: String,
): Serializable
