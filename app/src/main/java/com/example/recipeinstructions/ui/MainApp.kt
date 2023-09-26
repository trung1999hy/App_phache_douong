package com.example.recipeinstructions.ui

import android.app.Application
import android.provider.Settings
import com.example.recipeinstructions.local.Preference

class MainApp : Application() {
    var preference: Preference? = null
    override fun onCreate() {
        super.onCreate()
        instant = this
        preference = Preference.buildInstance(this)
        if (preference?.firstInstall == false) {
            preference?.firstInstall = true
            preference?.setValueCoin(30)
        }

    }

    companion object {
        private var instant: MainApp? = null
        fun newInstance(): MainApp? {
            if (instant == null) {
                instant = MainApp()
            }
            return instant
        }
    }
    val deviceId: String
        get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)


}