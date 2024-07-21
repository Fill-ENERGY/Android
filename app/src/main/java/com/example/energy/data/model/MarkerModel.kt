package com.example.energy.data.model

data class MarkerModel(
    var markerName: String,
    var longitude: Double, // 경도(Longitude)
    var latitude: Double, // 위도(Latitude)
    var bookMark: Boolean,
)
