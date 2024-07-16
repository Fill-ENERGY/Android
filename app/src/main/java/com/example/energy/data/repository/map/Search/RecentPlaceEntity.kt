package com.example.energy.data.repository.map.Search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recentPlace")
data class RecentPlaceEntity(
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    @ColumnInfo(name = "placeName") val placeName: String
)
