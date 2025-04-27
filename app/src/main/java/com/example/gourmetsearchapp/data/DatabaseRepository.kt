package com.example.gourmetsearchapp.data

import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getAllFavoriteRestaurant() : Flow<List<FavoriteRestaurant>>

    suspend fun insert(favoriteRestaurant: FavoriteRestaurant)

    suspend fun delete(favoriteRestaurant: FavoriteRestaurant)
}

class OfflineDatabaseRepository(private val dao : FavoriteRestaurantDao) : DatabaseRepository{
    override fun getAllFavoriteRestaurant() : Flow<List<FavoriteRestaurant>> = dao.getAllFavoriteRestaurants()

    override suspend fun insert(favoriteRestaurant: FavoriteRestaurant) = dao.insert(favoriteRestaurant)

    override suspend fun delete(favoriteRestaurant: FavoriteRestaurant) = dao.delete(favoriteRestaurant)
}