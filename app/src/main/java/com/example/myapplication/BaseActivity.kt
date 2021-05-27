package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

open class BaseActivity:AppCompatActivity() {
    init {
        setup()
    }

    private fun setup() {
        if (dNightMode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    companion object {
        var dNightMode = 1
        const val PREFS_FILE = ".co.uk.grangefencing.calculator.prefs"
        const val PREFS_NIGHT_MODE = "nightMode"
    }
}