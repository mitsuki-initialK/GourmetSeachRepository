package com.example.gourmetsearchapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteRestraints")
data class FavoriteRestrains (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name: String,
    val address: String,
    val access : String,
    val open : String,
    val logo : String,
)