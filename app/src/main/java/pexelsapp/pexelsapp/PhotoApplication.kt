package pexelsapp.pexelsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import pexelsapp.pexelsapp.APIs.API

@HiltAndroidApp
class PhotoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        API.init(this)
    }
}