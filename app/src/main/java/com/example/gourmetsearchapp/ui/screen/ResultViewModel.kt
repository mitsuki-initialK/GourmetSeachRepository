package com.example.gourmetsearchapp.ui.screen

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gourmetsearchapp.data.DatabaseRepository
import com.example.gourmetsearchapp.data.FavoriteRestaurant
import com.example.gourmetsearchapp.gourmetSearch.GourmetSearchRepository
import com.example.gourmetsearchapp.gourmetSearch.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface GourmetSearchState {
    data class Success(val restaurantList : List<Restaurant>) : GourmetSearchState
    data object Loading : GourmetSearchState
    data object Error : GourmetSearchState
}

data class ResultUiState(
    var gourmetSearchState : GourmetSearchState = GourmetSearchState.Loading
)

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val gourmetSearchRepository: GourmetSearchRepository,
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState = _uiState.asStateFlow()

    private var _lat = 0.00
    private var _lng = 0.00
    private var _range = 0

    init {
        getGourmetInfo(34.68, 135.520, 4)
    }

    fun getGourmetInfo(lat : Double = _lat, lng : Double = _lng, range : Int = _range) {
        _lat = lat
        _lng = lng
        _range = range

        viewModelScope.launch {
            _uiState.update { it.copy(gourmetSearchState = GourmetSearchState.Loading) }
            val newState = try {
                GourmetSearchState.Success(
                    gourmetSearchRepository.getRestaurantList(
                        lat = lat,
                        lng = lng,
                        range = range
                    )
                )
            } catch (e: Exception) {
                println(e)
                GourmetSearchState.Error
            }
            _uiState.update { it.copy(gourmetSearchState = newState)}
        }
    }

    private fun Restaurant.toFormat() : FavoriteRestaurant = FavoriteRestaurant (
        id = id,
        name = name,
        address = address,
        access = access,
        open = open,
        logo = logo,
    )

//    suspend fun addToFavoriteList(restaurant : Restaurant){
//        databaseRepository.insert(restaurant.toFormat())
//    }
//
//    fun getFavoriteList(){
//        databaseRepository.getAllFavoriteRestaurant()
//    }
}