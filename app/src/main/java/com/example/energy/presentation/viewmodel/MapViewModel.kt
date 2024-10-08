package com.example.energy.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.energy.data.repository.map.StationDetailModel
import com.example.energy.data.repository.map.StationMapModel

class MapViewModel: ViewModel() {
   //accessToken
   private var _accessToken = MutableLiveData<String>()
   fun setAccessToken(token: String){
      _accessToken.value = token
   }
   val getAccessToken: LiveData<String>
      get() = _accessToken

   //markerList
   private var _markerList = MutableLiveData<List<StationMapModel>>()
   fun setMarkerList(markerList: List<StationMapModel>){
      _markerList.value = markerList
   }
   val getMarkerList: LiveData<List<StationMapModel>>
      get() = _markerList

   //힌트 텍스트
   private var _searchHint = MutableLiveData<String>()
   fun setSearchHint(hintText: String){
      _searchHint.value = hintText
   }
   val searchHint: LiveData<String>
      get() = _searchHint

   //현재 위치
   private val _currentLocation = MutableLiveData<Location>()
   val getCurrentLocation: LiveData<Location>
      get() = _currentLocation

   fun setCurrentLocation(location: Location) {
      _currentLocation.value = location
   }

   //충전소 정보
   private var _stationDetailModel = MutableLiveData<StationDetailModel>()

   private var _stationLongitude = MutableLiveData<Double>() //길찾기
   private var _stationLatitude = MutableLiveData<Double>()   //길찾기

   //충전소 디테일 정보
   val getStationDetailModel: LiveData<StationDetailModel>
      get() = _stationDetailModel
   fun setStationDetailModel(stationDetailModel: StationDetailModel) {
      _stationDetailModel.value = stationDetailModel
   }

   //충전소 경도
   fun setStationLongitude(longtitude: Double){
      _stationLongitude.value = longtitude
   }
   val getStationLongitude: LiveData<Double>
      get() = _stationLongitude

   //충전소 위도
   fun setStationLatitude(latitude: Double){
      _stationLatitude.value = latitude
   }
   val getStationLatitude: LiveData<Double>
      get() = _stationLatitude


   //현재 위치에서 떨어진 거리 구하기
   fun getDistanceFromCurrentLocation(currentLatitude: Double, currentLongitude: Double): Float {
      val targetLatitude = _stationLatitude.value ?: return -1f
      val targetLongitude = _stationLongitude.value ?: return -1f

      val startPoint = Location("startPoint").apply {
         latitude = currentLatitude
         longitude = currentLongitude
      }
      val endPoint = Location("endPoint").apply {
         latitude = targetLatitude
         longitude = targetLongitude
      }

      return startPoint.distanceTo(endPoint)
   }
}