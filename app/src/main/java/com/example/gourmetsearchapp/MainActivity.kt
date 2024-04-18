package com.example.gourmetsearchapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gourmetsearchapp.ui.GourmetSearchApp
import com.example.gourmetsearchapp.ui.screen.GourmetSearchViewModel
import com.example.gourmetsearchapp.ui.theme.GourmetSearchAppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GourmetSearchAppTheme {
                val gourmetSearchViewModel: GourmetSearchViewModel = viewModel()
                gourmetSearchViewModel.getLocation(this)
                GourmetSearchApp(
                    gourmetSearchViewModel,
                    { gourmetSearchViewModel.getLocation(this) }
                )
            }
        }
    }
}
