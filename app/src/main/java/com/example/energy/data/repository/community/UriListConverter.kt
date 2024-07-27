package com.example.energy.data.repository.community

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class UriListConverter {
    //받아온 Uri를 String으로 변환
    @TypeConverter
    fun fromUriList(uriList: List<Uri>): String {
        return uriList.joinToString(",") { it.toString() }
    }

    //변환한 String을 다시 Uri로 변환
    @TypeConverter
    fun toUriList(data: String): List<Uri> {
        return data.split(",").map { Uri.parse(it) }
    }

//    @TypeConverter
//    fun fromUriList(uriList: List<Uri>): String {
//        val gson = Gson()
//        return gson.toJson(uriList)
//    }
//
//    @TypeConverter
//    fun toUriList(uriString: String): List<Uri> {
//        val listType = object : TypeToken<List<Uri>>() {}.type
//        return Gson().fromJson(uriString, listType)
//    }
}