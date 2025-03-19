package com.example.gourmetsearchapp.ui.screen


import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gourmetsearchapp.Location.LocationRepository
import com.example.gourmetsearchapp.GourmetSearch.GourmetSearchRepository
import com.example.gourmetsearchapp.GourmetSearch.Restaurant
import kotlinx.coroutines.launch


sealed interface SearchGourmetState {
    data class Success(val restaurantList : List<Restaurant>) : SearchGourmetState
    object Loading : SearchGourmetState
    object Error : SearchGourmetState
}

sealed interface GetLocationState {
    data class Success(val location: Location, val addressLine: String) : GetLocationState
    object Loading : GetLocationState
    object Error : GetLocationState
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class SearchViewModel(
    private val gourmetSearchRepository : GourmetSearchRepository,
    private val locationRepository: LocationRepository
) : ViewModel(){
    var searchGourmetState: SearchGourmetState by mutableStateOf(SearchGourmetState.Loading)
        private set

    var getLocationState : GetLocationState by mutableStateOf(GetLocationState.Loading)
        private set

    var rangeNum = 1


    init{
        getLocation()
    }

    fun getLocation() {
        viewModelScope.launch {
            getLocationState = GetLocationState.Loading
            getLocationState = try {
                val location : Location = locationRepository.getCoordinates()
                val addressLine : String = locationRepository.getAddressFromCoordinate(location.latitude, location.longitude)

                GetLocationState.Success(location, addressLine)
            } catch (e: Exception) {
                GetLocationState.Error
            }
        }
    }

    fun getGourmetInfo() {

        viewModelScope.launch {
            searchGourmetState = SearchGourmetState.Loading
            searchGourmetState = try {
                SearchGourmetState.Success(
                    gourmetSearchRepository.getRestaurantList(
                        lat = (getLocationState as GetLocationState.Success).location.latitude,
                        lng = (getLocationState as GetLocationState.Success).location.longitude,
                        range = rangeNum
                    )
                )
            } catch (e: Exception) {
                SearchGourmetState.Error
            }
        }
    }

//    companion object {
//        val Factory : ViewModelProvider.Factory = viewModelFactory{
//            initializer {
//                val application = (this[APPLICATION_KEY] as GourmetSearchApplication)
//                val gourmetSearchRepository = application.container.gourmetSearchRepository
//                val locationRepository = application.locationRepository
//                SearchViewModel(
//                    gourmetSearchRepository = gourmetSearchRepository,
//                    locationRepository = locationRepository
//                )
//            }
//        }
//    }

}