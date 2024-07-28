package com.example.energy.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel: ViewModel() {
   private var _searchHint = MutableLiveData<String>()
   val searchHint: LiveData<String>
      get() = _searchHint

   //충전소 정보
   private var _stationName = MutableLiveData<String>()  //이름
   private var _stationLongtitude = MutableLiveData<Double?>() //길찾기
   private var _stationLatitude = MutableLiveData<Double?>()   //길찾기
   private var _stationTime = MutableLiveData<String>()  //운영시간
   private var _stationCall = MutableLiveData<String>()  //전화번호

   fun setStationName(name: String){
      _stationName.value = name
   }

   val getStationName: LiveData<String>
      get() = _stationName






}