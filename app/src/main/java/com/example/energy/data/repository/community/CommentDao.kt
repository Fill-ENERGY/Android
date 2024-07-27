package com.example.energy.data.repository.community

import androidx.room.Dao
import androidx.room.DatabaseView
import androidx.room.Insert
import androidx.room.Query

@Dao
@DatabaseView
interface CommentDao {

    @Insert
    fun insertComment(comment: Comment)

    @Query("SELECT * FROM comment WHERE commentId = :id")
    fun getCommentById(id: Int): Comment?

    @Query("SELECT * FROM comment")
    fun getAllComments(): List<Comment>
}