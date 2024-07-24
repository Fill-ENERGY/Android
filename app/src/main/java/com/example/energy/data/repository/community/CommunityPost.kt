package com.example.energy.data.repository.community

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class CommunityPost(
    val userProfile: Int, //프로필 사진
    val userName: String, //사용자 이름
    val title: String, //제목
    val content: String, //내용
    val category: String, //카테고리
    val imageUrl: List<Uri>, //사진 리스트
    val likes: String, //좋아요 수
    val comments: String, //댓글 수
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Uri.CREATOR) ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userProfile)
        parcel.writeString(userName)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(category)
        parcel.writeTypedList(imageUrl)
        parcel.writeString(likes)
        parcel.writeString(comments)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CommunityPost> {
        override fun createFromParcel(parcel: Parcel): CommunityPost {
            return CommunityPost(parcel)
        }

        override fun newArray(size: Int): Array<CommunityPost?> {
            return arrayOfNulls(size)
        }
    }
}