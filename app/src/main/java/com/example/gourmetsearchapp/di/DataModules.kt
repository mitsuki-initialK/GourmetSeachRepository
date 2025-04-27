package com.example.gourmetsearchapp.di

import android.content.Context
import android.location.Location
import com.example.gourmetsearchapp.data.DatabaseRepository
import com.example.gourmetsearchapp.data.InventoryDatabase
import com.example.gourmetsearchapp.data.OfflineDatabaseRepository
import com.example.gourmetsearchapp.gourmetSearch.GourmetSearchApiService
import com.example.gourmetsearchapp.gourmetSearch.GourmetSearchRepository
import com.example.gourmetsearchapp.gourmetSearch.NetworkGourmetSearchRepository
import com.example.gourmetsearchapp.location.LocationRepository
import com.example.gourmetsearchapp.location.NetworkLocationRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GourmetSearchRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindGourmetSearchRepository(repository: NetworkGourmetSearchRepository): GourmetSearchRepository
}


@Module
@InstallIn(SingletonComponent::class)  //インスタンス寿命の設定　ここではアプリケーション全体
object ApiModule {

    @Provides  //インスタンス生成時に、自動で関数を実行
    @Singleton //形式的にprovidesとともにつける
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    fun provideRetrofit(json: Json): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("https://webservice.recruit.co.jp/hotpepper/gourmet/")
            .build()
    }

    @Provides
    fun provideGourmetApiService(retrofit: Retrofit): GourmetSearchApiService {
        return retrofit.create(GourmetSearchApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabaseRepository(@ApplicationContext context : Context) : DatabaseRepository {
        return OfflineDatabaseRepository(InventoryDatabase.getDatabase(context).favoriteRestaurantDao())
    }

}