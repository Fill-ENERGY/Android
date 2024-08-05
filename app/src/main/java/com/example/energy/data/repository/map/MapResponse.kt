package com.example.energy.data.repository.map

data class MapResponse (
    val code: String,
    val message: String,
    val result: StationMapModel
)

data class StationMapModel(
    var id: Long?,
    var name: String?,
    var latitude: Double?,
    var longitude: Double?,
)