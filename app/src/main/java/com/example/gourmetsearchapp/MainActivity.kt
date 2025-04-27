package com.example.gourmetsearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gourmetsearchapp.location.LocationRepository
import com.example.gourmetsearchapp.location.NetworkLocationRepository
import com.example.gourmetsearchapp.ui.GourmetSearchApp
import com.example.gourmetsearchapp.ui.theme.GourmetSearchAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val locationRepository = NetworkLocationRepository(this)

        setContent {
            GourmetSearchAppTheme {
                GourmetSearchApp(locationRepository)
            }
        }
    }
}

