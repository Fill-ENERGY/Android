package com.example.energy.data.repository.community

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import android.net.Uri

@Entity(tableName = "community_post")
data class CommunityPost(
    @PrimaryKey(autoGenerate = false) var id: Int = 0,
    var userProfile: Int? = null,
    var userName: String? = "",
    var title: String? = "",
    var content: String? = "",
    var categoryString: String? = "",
    var category: Int? = null,
    @TypeConverters(UriListConverter::class) var imageUrl: List<Uri> = emptyList(),
    var likes: String? = "",
    var comments: String? = "",
)
