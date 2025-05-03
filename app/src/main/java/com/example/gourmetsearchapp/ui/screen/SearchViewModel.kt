package com.example.gourmetsearchapp.ui.screen


import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gourmetsearchapp.location.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface GetLocationState {
    data class Success(val location: Location, val addressLine: String) : GetLocationState
    data object Loading : GetLocationState
    data object Error : GetLocationState
}

data class SearchUiState(
    var rangeNum: Int = 0,
    var getLocationState: GetLocationState = GetLocationState.Loading,
)


class SearchViewModel(
    private val locationRepository: LocationRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()


    init{
        getLocation()
    }


    fun updateUiState(newNum : Int){
        _uiState.update { currentState ->
            currentState.copy(
                rangeNum = newNum
            )
        }
    }

    fun getLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(getLocationState = GetLocationState.Loading) }
            val newState = try {
                val location : Location = locationRepository.getCoordinates()
                location.latitude = 34.686
                location.longitude = 135.520
                val addressLine : String = locationRepository.getAddressFromCoordinate(location.latitude, location.longitude)
                GetLocationState.Success(location, addressLine)
            } catch (e: Exception) {
                GetLocationState.Error
            }
            _uiState.update { it.copy(getLocationState = newState) }
        }
    }

}