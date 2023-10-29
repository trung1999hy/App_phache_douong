package com.example.recipeinstructions.ui

import android.app.Application
import android.provider.Settings
import com.example.recipeinstructions.local.Preference

class MainApp : Application() {
    var preference: Preference? = null
    var preferenceNews: Preference? = null
    override fun onCreate() {
        super.onCreate()
        instant = this
        instantNews = this
        preference = Preference.buildInstance(this)
        preferenceNews = Preference.buildInstanceNews(this)
        if (preference?.firstInstall == false) {
            preference?.firstInstall = true
            preference?.setValueCoin(30)
        }
        if (preferenceNews?.firstInstallNews == false) {
            preferenceNews?.firstInstallNews = true
            preferenceNews?.setValueCoinNews(20)
        }

    }

    companion object {
        private var instant: MainApp? = null
        private var instantNews: MainApp? = null
        fun newInstance(): MainApp? {
            if (instant == null) {
                instant = MainApp()
            }
            return instant
        }

        fun newInstanceNews(): MainApp? {
            if (instantNews == null) {
                instantNews = MainApp()
            }
            return instantNews
        }
    }

    val deviceId: String
        get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    val deviceIdNews: String
        get() = (Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) + "1")

}