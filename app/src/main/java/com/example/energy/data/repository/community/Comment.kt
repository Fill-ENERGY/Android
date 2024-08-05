package com.example.energy.data.repository.community

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment")
data class Comment(
    @PrimaryKey(autoGenerate = true) var commentId: Int = 0,
    var userInfo: String = "", //UserInfo?,
    var body: String = "",
    val parentCommentId : Int? = null,
    var createTime: String = "",
)
