package com.example.magmarket.application

import android.app.Application
import android.graphics.Typeface
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application() {
    override fun onCreate() {
        getTypeFace()
        super.onCreate()
    }
    fun getTypeFace(): Typeface {
        return Typeface.createFromAsset(resources.assets, "fonts/far_mitra.ttf");
    }
}