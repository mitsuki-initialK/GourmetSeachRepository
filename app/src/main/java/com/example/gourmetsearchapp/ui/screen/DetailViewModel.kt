package com.example.gourmetsearchapp.ui.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gourmetsearchapp.gourmetSearch.GourmetSearchRepository
import com.example.gourmetsearchapp.gourmetSearch.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    gourmetSearchRepository: GourmetSearchRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val restaurantId: String = savedStateHandle["restaurantId"]!!

    val restaurant : Restaurant = gourmetSearchRepository.getRestaurantById(restaurantId)!!
}
