package com.example.energy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel: ViewModel() {
    //accessToken
    private var _accessToken = MutableLiveData<String>()
    fun setAccessToken(token: String){
        _accessToken.value = token
    }
    val getAccessToken: LiveData<String>
        get() = _accessToken

    //충전소 이름
    private var _stationName = MutableLiveData<String>()
    fun setStationName(name: String){
        _stationName.value = name
    }
    val getStationName: LiveData<String>
        get() = _stationName

    //충전소 ID
    private var _stationId = MutableLiveData<Int>()
    fun setStationId(id: Int){
        _stationId.value = id
    }
    val getStationId: LiveData<Int>
        get() = _stationId


}