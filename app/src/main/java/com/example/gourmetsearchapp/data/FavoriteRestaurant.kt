package com.example.gourmetsearchapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteRestaurants")
data class FavoriteRestaurant (
    @PrimaryKey
    val id : String,
    val name: String,
    val address: String,
    val access : String,
    val open : String,
    val logo : String,
)