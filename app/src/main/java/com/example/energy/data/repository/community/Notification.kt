package com.example.energy.data.repository.community

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Notification(
    var id: Int = 0,
    var userProfile: Int? = null,
    var userName: String? = "",
    var isComment: Boolean? = true, // test용
)