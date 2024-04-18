package com.example.gourmetsearchapp.gourmetSearchAPI

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class Results(
    val results: ApiInfo
)

@Serializable
data class ApiInfo(
    @SerialName("api_version") val apiVersion: String,
    @SerialName("results_available") val resultsAvailable: Int,
    @SerialName("results_returned") val resultsReturned: String,
    @SerialName("results_start") val resultsStart: Int,
    val shop : List<Restaurant>
)

@Serializable
data class Restaurant(
    val name: String,
    val address: String,
    val access : String,
    val open : String,
    @SerialName("logo_image") val logo : String,
    val photo : Device,
)

@Serializable
data class Device(
    val pc: Map<String, String>,
    val mobile: Map<String, String>
)