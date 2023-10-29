package com.example.recipeinstructions.local

import android.content.Context
import android.content.SharedPreferences

class Preference(context: Context) {
    private val BEARER_HEADER = "Bearer0 "
    private val sharedPreferences: SharedPreferences
    private val PREFS_ACCOUNT = "PREFS_ACCOUNT"
    private val PREFS_ACCOUNT_NEWS = "PREFS_ACCOUNT_NEWS"
    private val KEY_TYPE_ONE = "KEY_TYPE_ONE"
    private val KEY_TYPE_ONE_NEWS = "KEY_TYPE_ONE_NEWS"
    private val KEY_TOTAL_COIN = "KEY_TOTAL_COIN" // coin
    private val KEY_TOTAL_COIN_NEWS = "KEY_TOTAL_COIN_NEWS" // coin
    private val KEY_FIRST_INSTALL = "KEY_FIRST_INSTALL" // coin
    private val KEY_FIRST_INSTALL_NEWS = "KEY_FIRST_INSTALL_NEWS" // coin
    private val INT_ZERO = 0 // coin

    private val sharedPreferencesNews: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_ACCOUNT, Context.MODE_PRIVATE)
        sharedPreferencesNews = context.getSharedPreferences(PREFS_ACCOUNT_NEWS, Context.MODE_PRIVATE)
    }

    fun setValueTypeOne(value: String?) {
        sharedPreferences.edit().putString(KEY_TYPE_ONE, value).apply()
    }

    fun setValueCoinNews(value: String?) {
        sharedPreferencesNews.edit().putString(KEY_TYPE_ONE_NEWS, value).apply()
    }

    var firstInstall: Boolean
        get() = sharedPreferences.getBoolean(KEY_FIRST_INSTALL, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_FIRST_INSTALL, value).apply()
        }

    var firstInstallNews: Boolean
        get() = sharedPreferencesNews.getBoolean(KEY_FIRST_INSTALL_NEWS, false)
        set(value) {
            sharedPreferencesNews.edit().putBoolean(KEY_FIRST_INSTALL_NEWS, value).apply()
        }

    fun setValueCoin(value: Int) {
        sharedPreferences.edit().putInt(KEY_TOTAL_COIN, value).apply()
    }

    fun setValueCoinNews(value: Int) {
        sharedPreferencesNews.edit().putInt(KEY_TOTAL_COIN_NEWS, value).apply()
    }

    fun getValueCoin(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_COIN, INT_ZERO)
    }

    fun getValueCoinNews(): Int {
        return sharedPreferencesNews.getInt(KEY_TOTAL_COIN_NEWS, INT_ZERO)
    }


    companion object {
        var instance: Preference? = null
        var instanceNews: Preference? = null
        fun buildInstance(context: Context): Preference? {
            if (instance == null) {
                instance = Preference(context)
            }
            return instance
        }

        fun buildInstanceNews(context: Context): Preference? {
            if (instanceNews == null) {
                instanceNews = Preference(context)
            }
            return instanceNews
        }
    }
}