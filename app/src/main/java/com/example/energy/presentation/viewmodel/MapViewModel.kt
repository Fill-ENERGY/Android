package com.example.energy.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel: ViewModel() {
   private var _searchHint = MutableLiveData<String>()
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
   private var _stationName = MutableLiveData<String>()  //이름
   private var _stationLongitude = MutableLiveData<Double>() //길찾기
   private var _stationLatitude = MutableLiveData<Double>()   //길찾기
   private var _stationTime = MutableLiveData<String>()  //운영시간
   private var _stationCall = MutableLiveData<String>()  //전화번호

   //충전소 이름
   fun setStationName(name: String){
      _stationName.value = name
   }
   val getStationName: LiveData<String>
      get() = _stationName

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

   //충전소 운영 시간
   fun setStationTime(time: String){
      _stationTime.value = time
   }
   val getStationTime: LiveData<String>
      get() = _stationTime

   //충전소 전화번호
   fun setStationCall(callNumber: String){
      _stationCall.value = callNumber
   }
   val getStationCall: LiveData<String>
      get() = _stationCall

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