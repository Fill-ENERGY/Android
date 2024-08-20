package com.example.energy.data.repository.list

import com.example.energy.data.repository.map.ListMapModel


//충전소 전체 가져오는 API
data class ListResponse (
    val code: String,
    val message: String,
    val result: Result

)

data class Result (

    val stations: List<ListMapModel>,
    val lastId: Int,
    val hasNext: Boolean

)

data class ListMapModel(

    var id: Int?,
    var name: String?,
    var distance: String?,
    var score: Double?,
    var scoreCount: Int?,
    var latitude: Double?,
    var longitude: Double?,
    val dayOfWeek: String,
    val openTime: String,
    var closeTime: String?,
    var institutionPhone: String?
)




//충전소 개별 조회
data class StationResponse (
    val code: String,
    val message: String,
    val result: StationDetailModel
)

data class StationDetailModel (
    var id: Int?,
    var name: String?,
    var distance: String?,
    var score: Double?,
    var scoreCount: Int?,
    var address: String?,
    var streetNumber: String?,
    var weekdayOpen: String?,
    var weekdayClose: String?,
    var saturdayOpen: String?,
    var saturdayClose: String?,
    var holidayOpen: String?,
    var holidayClose: String?,
    var phoneNumber: String?,
    var concurrentUsageCount: Int?,
    var airInjectionAvailable: Boolean?,
    var phoneChargingAvailable: Boolean?,
    var favorite: Boolean?
)
