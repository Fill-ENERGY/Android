package com.example.energy.data.repository.map.Search

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecentPlaceEntity::class], version = 1)
abstract class RecentPlaceDataBase : RoomDatabase() {
    abstract fun recentPlaceDao() : SearchDao

    companion object {

        private var instance: RecentPlaceDataBase? = null

        @Synchronized
        fun getInstance(context: Context): RecentPlaceDataBase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    RecentPlaceDataBase::class.java, "RecentPlaceDB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }
}