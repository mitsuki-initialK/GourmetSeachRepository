package com.example.gourmetsearchapp.gourmetSearchAPI

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface AppContainer{
    val gourmetSearchRepository : GourmetSearchRepository
}

class GourmetSearchContainer : AppContainer{

    private val baseUrl =
        "https://webservice.recruit.co.jp/hotpepper/gourmet/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : GourmetSearchApiService by lazy {
        retrofit.create(GourmetSearchApiService::class.java)
    }

    override val gourmetSearchRepository: GourmetSearchRepository by lazy {
        NetworkGourmetSearchRepository(retrofitService)
    }

}