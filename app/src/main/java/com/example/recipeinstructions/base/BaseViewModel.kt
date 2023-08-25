package com.example.recipeinstructions.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.recipeinstructions.network.config.NetworkInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    val message : MutableLiveData<String> = MutableLiveData()

    private fun cleanUp() {
        scope.cancel()
    }
    val Throwable.msgError: String
        get() {
            return when (this) {
                is NetworkInterceptor.NoConnectivityException -> "Không có kết nối mạng.\nKiểm tra lại kết nối 3G/WIFI của bạn"
                is ConnectException -> "Không thể kết nối đến server\nVui lòng thử lại."
                is SocketTimeoutException -> "Không thể kết nối đến server\nVui lòng thử lại."
                is UnknownHostException -> "Không thể kết nối đến server\nVui lòng thử lại."
                is HttpException -> {
                    when (this.code()) {
                        HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                            "Server đang bảo trì.Vui lòng thử lại sau ít phút"
                        }
                        else -> this.response()?.message() ?: "unknown error"
                    }
                }
                else -> this.message ?: "unknown error"
            }
        }


    override fun onCleared() {
        super.onCleared()
        cleanUp()
    }
}