package com.example.gourmetsearchapp.GourmetSearch


interface  GourmetSearchRepository{
    suspend fun getRestaurantList(lat : Double, lng : Double, range : Int) : List<Restaurant>
}

class NetworkGourmetSearchRepository(
    private val gourmetSearchApiService: GourmetSearchApiService
) : GourmetSearchRepository{

    override suspend fun getRestaurantList(lat : Double, lng : Double, range : Int): List<Restaurant> =
        gourmetSearchApiService.searchGourmet(lat = lat, lng = lng, range = range).results.shop

}