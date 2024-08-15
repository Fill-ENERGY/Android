package com.example.energy.data.repository.review

data class PostReviewBody(
    var content: String,
    var score: Double,
    var keywords: List<String>?,
    var stationId: Int,
    var imgUrls: List<String>?
)

data class PatchReviewBody(
    var content: String,
    var score: Double,
    var keywords: List<String>?,
    //var stationId: Int,
    var imgUrls: List<String>?
)

