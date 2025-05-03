package com.example.gourmetsearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gourmetsearchapp.ui.theme.GourmetSearchAppTheme
import com.example.gourmetsearchapp.ui.GourmetSearchNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            GourmetSearchAppTheme {
                GourmetSearchNavGraph()
            }
        }
    }
}

