package com.example.gourmetsearchapp.gourmetSearchAPI

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

val json = Json {
    ignoreUnknownKeys = true
}

private const val BASE_URL =
    "https://webservice.recruit.co.jp/hotpepper/gourmet/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

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

object GourmetSearchApi {
    val retrofitService : GourmetSearchApiService by lazy {
        retrofit.create(GourmetSearchApiService::class.java)
    }
}





