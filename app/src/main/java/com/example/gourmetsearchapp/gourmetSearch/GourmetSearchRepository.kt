package com.example.gourmetsearchapp.gourmetSearch

import jakarta.inject.Inject
import javax.inject.Singleton


interface  GourmetSearchRepository{
    suspend fun getRestaurantList(lat : Double, lng : Double, range : Int) : List<Restaurant>
    fun getRestaurantById(id : String) : Restaurant?
}

@Singleton
class NetworkGourmetSearchRepository @Inject constructor(
    private val gourmetSearchApiService: GourmetSearchApiService
) : GourmetSearchRepository{

     var restaurantList : List<Restaurant> = listOf()

    override suspend fun getRestaurantList(lat : Double, lng : Double, range : Int): List<Restaurant>{
        restaurantList = gourmetSearchApiService.searchGourmet(lat = lat, lng = lng, range = range).results.shop

        restaurantList.forEach{
            it.calcDistance(lat, lng)
        }

        return restaurantList
    }

    override fun getRestaurantById(id: String): Restaurant? {
        val restaurant = restaurantList.find { it.id == id }
        return restaurant
    }


}