package com.example.energy.data.repository.profile

data class ProfileResponse(
    val code: String,
    val message: String,
    val result: MyPageProfileModel,
)

data class MyPageProfileModel(
    var profileImg: String?,
    var createdAt: String?,
    var updatedAt: String?,
    val name: String?,
    val nickname: String?,
    val gender: String?,
    val birth: String?,
    var memberType: String?,
    )
