package com.example.gourmetsearchapp.gourmetSearch

import jakarta.inject.Inject
import javax.inject.Singleton


interface  GourmetSearchRepository{
    suspend fun getRestaurantList(lat : Double, lng : Double, range : Int) : List<Restaurant>
}

@Singleton
class NetworkGourmetSearchRepository @Inject constructor(
    private val gourmetSearchApiService: GourmetSearchApiService
) : GourmetSearchRepository{

//    var lat = 0.00
//    var lng = 0.0
//    var range = 1
//    var restaurantList : List<Restaurant> = listOf()

    override suspend fun getRestaurantList(lat : Double, lng : Double, range : Int): List<Restaurant>{
        val restaurantList = gourmetSearchApiService.searchGourmet(lat = lat, lng = lng, range = range).results.shop

//        this.lat = lat
//        this.lng = lng
//        this.range = range

        restaurantList.forEach{
            it.calcDistance(lat, lng)
        }

        //this.restaurantList = restaurantList

        return restaurantList
    }
}