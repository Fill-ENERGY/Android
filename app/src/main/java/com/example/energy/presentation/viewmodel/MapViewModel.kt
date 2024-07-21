package com.example.energy.presentation.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel: ViewModel() {
   private var _searchHint = MutableLiveData<String>()
   val searchHint: LiveData<String>
      get() = _searchHint

}