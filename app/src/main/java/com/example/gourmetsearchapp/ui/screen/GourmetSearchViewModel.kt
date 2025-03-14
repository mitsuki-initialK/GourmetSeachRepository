package com.example.gourmetsearchapp.ui.screen

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gourmetsearchapp.GourmetSearchApplication
import com.example.gourmetsearchapp.MainActivity
import com.example.gourmetsearchapp.geoCoder.LocationGetter
import com.example.gourmetsearchapp.gourmetSearchAPI.GourmetSearchRepository
import com.example.gourmetsearchapp.gourmetSearchAPI.Restaurant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale


sealed interface SearchGourmetState {
    data class Success(val restaurantList : List<Restaurant>) : SearchGourmetState
    object Loading : SearchGourmetState
    object Error : SearchGourmetState
}


class GourmetSearchViewModel(private val gourmetSearchRepository : GourmetSearchRepository) : ViewModel(){

    var searchGourmetState: SearchGourmetState by mutableStateOf(SearchGourmetState.Loading)
        private set

    private var lat = 0.00
    private var lng = 0.00
    var rangeNum = 1

    private var _addressLine = MutableStateFlow("")
    val addressLine: StateFlow<String> = _addressLine.asStateFlow()
    var locationAvailable = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getLocation(activity : MainActivity){
        val locationGetter = LocationGetter(activity)
        locationGetter.requestLocationPermission(activity)
        locationGetter.fusedLocation()
        locationGetter.location.observe(activity) {
            lat = it.latitude
            lng = it.longitude

            //緯度経度から住所を取得
            val geocoder = Geocoder(activity, Locale.getDefault())
            try {
                val addresses: MutableList<Address>? = geocoder.getFromLocation(lat, lng, 1)
                if (addresses != null) {
                    if (addresses.isNotEmpty()) {
                        val address: Address = addresses[0]
                        if (address.getAddressLine(0) != null) {
                            _addressLine.value = address.getAddressLine(0)
                            locationAvailable = true
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getGourmetInfo() {
        viewModelScope.launch {
            searchGourmetState = SearchGourmetState.Loading
            searchGourmetState = try {
                SearchGourmetState.Success(
                    gourmetSearchRepository.getRestaurantList(
                        lat = lat,
                        lng = lng,
                        range = rangeNum
                    )
                )
            } catch (e: IOException) {
                SearchGourmetState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GourmetSearchApplication)
                val gourmetSearchRepository = application.container.gourmetSearchRepository
                GourmetSearchViewModel(gourmetSearchRepository = gourmetSearchRepository)
            }
        }
    }

}