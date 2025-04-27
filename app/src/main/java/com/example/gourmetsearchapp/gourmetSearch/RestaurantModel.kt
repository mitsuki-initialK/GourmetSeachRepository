package com.example.gourmetsearchapp.gourmetSearch

import android.location.Location
import androidx.navigation.ActivityNavigator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class Result(
    val results : Results
)

@Serializable
data class Results(
    val shop : List<Restaurant>
)

@Serializable
data class Restaurant(
    val id : String,
    val name: String,
    val address: String,
    val access : String,
    val open : String,
    @SerialName("logo_image") val logo : String,
    val lat : Double,
    val lng : Double,
    val photo : Device,
    @Transient var distance: Int = 0,
)

@Serializable
data class Device(
    val pc: Map<String, String>,
    val mobile: Map<String, String>
)

fun Restaurant.calcDistance(currentLat : Double, currentLng : Double){
    val start = Location("").apply {
        latitude = currentLat
        longitude = currentLng
    }

    val end = Location("").apply {
        latitude = lat
        longitude = lng
    }

    distance = start.distanceTo(end).toInt() // 距離（m）で取得
}
