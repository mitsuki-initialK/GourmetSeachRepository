package com.example.gourmetsearchapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRestaurantDao {
    @Query("SELECT * from favoriteRestaurants")
    fun getAllFavoriteRestaurants() : Flow<List<FavoriteRestaurant>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteRestaurant: FavoriteRestaurant)

    @Delete
    suspend fun delete(favoriteRestaurant: FavoriteRestaurant)
}