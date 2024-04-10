package com.example.gourmetsearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gourmetsearchapp.model.GourmetSearchViewModel
import com.example.gourmetsearchapp.ui.GourmetSearchApp
import com.example.gourmetsearchapp.ui.theme.GourmetSearchAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GourmetSearchAppTheme {
                val gourmetSearchViewModel: GourmetSearchViewModel = viewModel()
                gourmetSearchViewModel.getLocation(this)
                GourmetSearchApp(
                    gourmetSearchViewModel
                )
            }
        }
    }
}
