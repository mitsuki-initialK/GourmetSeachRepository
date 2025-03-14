package com.example.gourmetsearchapp

import android.app.Application
import com.example.gourmetsearchapp.gourmetSearchAPI.AppContainer
import com.example.gourmetsearchapp.gourmetSearchAPI.GourmetSearchContainer

class GourmetSearchApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = GourmetSearchContainer()
    }
}