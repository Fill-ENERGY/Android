package com.example.energy.data.repository.community

import androidx.room.TypeConverter
import com.example.energy.R

class CategoryConverter {

    @TypeConverter
    fun fromString(category: String): Int {
        return when (category) {
            "일상" -> R.drawable.tag_daily
            "궁금해요" -> R.drawable.tag_curious
            "도와줘요" -> R.drawable.tag_help
            "휠체어" -> R.drawable.tag_wheelchair
            else -> R.drawable.tag_scooter
        }
    }
}