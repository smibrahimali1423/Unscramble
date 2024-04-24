package com.hfad.unscramble

import android.app.Application
import com.hfad.unscramble.data.AppContainer

class UnscrambleApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(context = this)
    }
}