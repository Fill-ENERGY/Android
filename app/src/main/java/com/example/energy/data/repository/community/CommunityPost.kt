package com.example.energy.data.repository.community

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "community_post")
data class CommunityPost(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var userProfile: Int? = null,
    var userName: String? = "",
    var title: String? = "",
    var content: String? = "",
    var categoryString: String? = "",
    var category: Int? = null,
    @TypeConverters(UriListConverter::class) var imageUrl: List<Uri> = emptyList(),
//    var imageUrl: List<String> = emptyList(),
    var likes: String? = "",
    var comments: String? = "",
)
