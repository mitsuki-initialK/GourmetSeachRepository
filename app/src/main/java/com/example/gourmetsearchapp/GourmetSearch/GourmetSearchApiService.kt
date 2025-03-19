package com.example.gourmetsearchapp.GourmetSearch

import retrofit2.http.GET
import retrofit2.http.Query

interface GourmetSearchApiService {
    @GET("v1/")
    suspend fun searchGourmet(
        @Query("key") apiKey: String = "310263465a27ea66",
        @Query("format") format: String = "json",
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("range") range: Int,  //１～５で指定
        @Query("count") count : Int = 100, //最大100件まで表示
    ) : Results
}






