package com.example.energy.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.energy.data.repository.map.Search.RecentPlaceDataBase
import com.example.energy.data.repository.map.Search.RecentPlaceEntity
import kotlinx.coroutines.launch

class SearchViewModel (private val recentPlaceDB: RecentPlaceDataBase) : ViewModel() {
    val recentSearches = MutableLiveData<List<RecentPlaceEntity>>()

    init {
        loadRecentSearches()
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            recentSearches.postValue(recentPlaceDB.recentPlaceDao().getRecentSearches())
        }
    }

    fun addSearchKeyword(placeName: RecentPlaceEntity) {
        viewModelScope.launch {
            recentPlaceDB.recentPlaceDao().insertSearch(recentPlace = placeName)
            loadRecentSearches()
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            recentPlaceDB.recentPlaceDao().clearSearchHistory()
            loadRecentSearches()
        }
    }
}