package com.example.energy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.energy.data.repository.community.Comment
import com.example.energy.data.repository.community.CommentDao
import com.example.energy.data.repository.community.CommunityPost
import com.example.energy.data.repository.community.CommunityPostDao
import com.example.energy.data.repository.community.UriListConverter
import com.example.energy.data.repository.community.WritingCommunityImage

@Database(entities = [CommunityPost::class],[Comment::class], version = 5)
@TypeConverters(UriListConverter::class)
abstract class CommunityPostDatabase : RoomDatabase() {

    abstract fun communityPostDao(): CommunityPostDao
    abstract fun commentDao(): CommentDao

    companion object {
        private var INSTANCE: CommunityPostDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CommunityPostDatabase? {
//            if (instance == null) {
//                synchronized(CommunityPostDatabase::class){
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        CommunityPostDatabase::class.java,
//                        "community-database"//다른 데이터 베이스랑 이름겹치면 꼬임
//                    ).fallbackToDestructiveMigration().build()
//                }
//            }
//
//            return instance
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CommunityPostDatabase::class.java,
                    "community-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}