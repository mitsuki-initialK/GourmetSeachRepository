package com.example.gourmetsearchapp

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.gourmetsearchapp.Location.LocationRepository
import com.example.gourmetsearchapp.GourmetSearch.GourmetSearchContainer
import com.example.gourmetsearchapp.ui.GourmetSearchApp
import com.example.gourmetsearchapp.ui.theme.GourmetSearchAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val locationRepository = LocationRepository(this)
        val gourmetSearchRepository = GourmetSearchContainer().gourmetSearchRepository

        setContent {
            GourmetSearchAppTheme {
                GourmetSearchApp(
                    gourmetSearchRepository = gourmetSearchRepository,
                    locationRepository = locationRepository
                )
            }
        }
    }
}
