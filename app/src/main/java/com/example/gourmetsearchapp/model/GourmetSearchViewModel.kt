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

    private var lat = 0.00
    private var lng = 0.00
    var rangeNum = 1

    fun getLocation(activity : MainActivity){
        val locationGetter = LocationGetter(activity)
        locationGetter.requestLocationPermission(activity)
        locationGetter.fusedLocation()
        locationGetter.location.observe(activity, Observer {
            lat = it.latitude
            lng = it.longitude
        })
    }

    fun getGourmetInfo() {
        viewModelScope.launch {
            gourmetSearchUiState = try{
                val listResult = GourmetSearchApi.retrofitService.searchGourmet(
                    lat = lat,
                    lng = lng,
                    range = rangeNum
                ).results.shop
                GourmetSearchUiState.Success(listResult)
            } catch (e: IOException) {
                GourmetSearchUiState.Error
            }
        }
    }

}