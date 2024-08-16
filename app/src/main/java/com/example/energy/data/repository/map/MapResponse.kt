package com.example.energy.data.repository.map


//충전소 전체 가져오는 API
data class ListResponse (
    val code: String,
    val message: String,
    val result: List<ListMapModel>
)

data class ListMapModel (
    var id: Int?,
    var name: String?,
    var distance: String?,
    var score: Double?,
    var scoreCount: Int?,
    var latitude: Double?,
    var longitude: Double?,
    var openTime: String?,
    var closeTime: String?
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
    var latitude: Double?,
    var longitude: Double?,
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

//즐겨찾기한 충전소 조회
data class StationBookmarkResponse (
    val code: String,
    val message: String,
    val result: List<StationBookmarkModel>?
)

data class StationBookmarkModel (
    var id: Int?,
    var name: String?,
    var distance: String?,
    var score: Double?,
    var scoreCount: Int?,
    var latitude: Double?,
    var longitude: Double?,
    var dayOfWeek: String?,
    var openTime: String?,
    var closeTime: String?,
)


//지도에 표시할 모든 충전소 정보 조회
data class MapResponse (
    val code: String,
    val message: String,
    val result: List<StationMapModel>
)

data class StationMapModel(
    var id: Int?,
    var name: String?,
    var latitude: Double?,
    var longitude: Double?,
)