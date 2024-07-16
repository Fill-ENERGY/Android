package com.example.energy.data.repository.map.Search

import androidx.room.Insert
import androidx.room.Query

interface SearchDao {
    @Query("SELECT * FROM recentPlace ORDER BY id DESC LIMIT 10")
    suspend fun getRecentSearches(): List<RecentPlaceEntity>

    @Insert
    suspend fun insertSearch(recentPlace: RecentPlaceEntity)

    @Query("DELETE FROM recentPlace")
    suspend fun clearSearchHistory()
}