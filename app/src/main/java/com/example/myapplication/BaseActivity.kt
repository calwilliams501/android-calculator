package com.example.myapplication

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
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

    fun longVibrationPattern() {
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(300, 100,300, 100,300, 100,300, 100,300, 100,300, 100), intArrayOf(255, 0,255, 0,255, 0,255, 0,255, 0,255, 0), -1))
    }

    fun onClickVibration() {
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(50), intArrayOf(100), -1))
    }


    companion object {
        var dNightMode = 1
        const val PREFS_FILE = ".co.uk.grangefencing.calculator.prefs"
        const val PREFS_NIGHT_MODE = "nightMode"
    }
}