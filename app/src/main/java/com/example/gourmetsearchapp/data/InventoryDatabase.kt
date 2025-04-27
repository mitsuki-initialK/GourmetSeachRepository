package com.example.gourmetsearchapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteRestaurant::class], version = 1, exportSchema = false)
abstract class InventoryDatabase() : RoomDatabase() {
    abstract fun favoriteRestaurantDao() : FavoriteRestaurantDao

    companion object {
        @Volatile
        private var Instance : InventoryDatabase? = null

        fun getDatabase(context: Context) : InventoryDatabase {
            return Instance ?: synchronized(this) {
                val _Instance = Room.databaseBuilder(context, InventoryDatabase::class.java, "favoriteRestaurants_database")
                    .fallbackToDestructiveMigration(true)
                    .build()

                Instance = _Instance
                _Instance
            }
        }
    }
}