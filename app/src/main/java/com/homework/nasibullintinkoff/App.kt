package com.homework.nasibullintinkoff

import android.app.Application
import android.content.Context
import com.homework.nasibullintinkoff.network.ApiService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){

    init {
        instance = this
    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

    }

    val apiService: ApiService by lazy { ApiService.create() }

    companion object {
        lateinit var appContext: Context
            private set
        lateinit var instance:App
            private set
    }

}