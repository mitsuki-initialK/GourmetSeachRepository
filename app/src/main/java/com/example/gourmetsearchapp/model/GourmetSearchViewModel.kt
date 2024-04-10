package com.example.gourmetsearchapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gourmetsearchapp.MainActivity
import com.example.gourmetsearchapp.network.GourmetSearchApi
import com.example.gourmetsearchapp.network.LocationGetter
import kotlinx.coroutines.launch
import java.io.IOException


sealed interface GourmetSearchUiState {
    data class Success(val restaurantList : List<Restaurant>) : GourmetSearchUiState
    object Loading : GourmetSearchUiState
    object Error : GourmetSearchUiState
}

class GourmetSearchViewModel : ViewModel() {

    var gourmetSearchUiState: GourmetSearchUiState by mutableStateOf(GourmetSearchUiState.Loading)
        private set

    private var latitude = 35.00
    private var longitude = 135.92
    var rangeNum = 1

    fun getLocation(activity : MainActivity){
        val locationGetter = LocationGetter(activity)
        locationGetter.requestLocationPermission(activity)
        locationGetter.fusedLocation()
        locationGetter.location.observe(activity, Observer {
            //latitude = it.latitude
            //longitude = it.longitude
        })
    }

    fun getGourmetInfo() {
        viewModelScope.launch {
            gourmetSearchUiState = try{
                val listResult = GourmetSearchApi.retrofitService.searchGourmet(
                    lat = latitude,
                    lng = longitude,
                    range = rangeNum
                ).results.shop
                GourmetSearchUiState.Success(listResult)
            } catch (e: IOException) {
                GourmetSearchUiState.Error
            }
        }
    }

}