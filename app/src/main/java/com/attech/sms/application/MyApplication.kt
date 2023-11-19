package com.attech.sms.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.attech.sms.utils.SharedPreferencesManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initValues()
    }

    private fun initValues() {
        SharedPreferencesManager.initialize(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}