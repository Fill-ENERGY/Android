package com.example.energy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.data.repository.community.CommunityPostDao
import com.example.energy.data.repository.community.UriListConverter
import com.example.energy.data.repository.community.WritingCommunityImage

@Database(entities = [CommunityPost::class], version = 3)
@TypeConverters(UriListConverter::class)
abstract class CommunityPostDatabase : RoomDatabase() {

    abstract fun communityPostDao(): CommunityPostDao

    companion object {
        private var instance: CommunityPostDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CommunityPostDatabase? {
            if (instance == null) {
                synchronized(CommunityPostDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CommunityPostDatabase::class.java,
                        "community-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ).allowMainThreadQueries().build()
                }
            }

            return instance
        }
    }
}