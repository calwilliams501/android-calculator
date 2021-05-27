package com.example.myapplication

import android.app.Application

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        val prefs = this.getSharedPreferences(".co.uk.grangefencing.calculator.prefs", 0)
        BaseActivity.dNightMode = 2
//            ^^^prefs.getInt("nightMode", 1)
    }
}