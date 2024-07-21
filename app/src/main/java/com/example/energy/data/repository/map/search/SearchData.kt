package com.example.energy.data.repository.map.search

data class SearchData(
    val location: String,
    val road: String, // 도로명 주소
    val address: String, // 지번 주소
    val x: Double, // 경도(Longitude)
    val y: Double, // 위도(Latitude)
)
