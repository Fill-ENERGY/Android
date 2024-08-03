package com.example.energy.data.repository.community

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_notification")
data class Notification(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var userProfile: Int? = null,
    var userName: String? = "",
    var isSingle: Boolean? = true, // test용
)
