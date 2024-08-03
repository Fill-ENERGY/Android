package com.example.energy.data.repository.community

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CommunityPostDao {
    @Insert
    fun insertPost(post: CommunityPost)

    @Query("SELECT * FROM community_post WHERE id = :id")
    fun getPostById(id: Int): CommunityPost

    @Query("SELECT * FROM community_post")
    fun getAllPosts(): List<CommunityPost>
}