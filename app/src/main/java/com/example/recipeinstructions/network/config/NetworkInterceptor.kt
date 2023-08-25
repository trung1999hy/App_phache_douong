package com.example.recipeinstructions.network.config

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.recipeinstructions.R

import okhttp3.Interceptor
import okhttp3.Response


import java.io.IOException

class NetworkInterceptor constructor(val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        when {
            !hasNetworkConnection(context) -> {
                throw NoConnectivityException(context)

            }

            else -> {
                return chain.proceed(
                    chain.request().newBuilder().build()
                )
            }
        }
    }

    fun hasNetworkConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            ) return true
        }
        return false
    }


    class NoConnectivityException(val context: Context) : IOException() {
        override fun getLocalizedMessage(): String? {
            return context.resources.getString(R.string.no_internet_connection)
        }
    }
}