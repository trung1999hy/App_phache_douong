package com.example.recipeinstructions.network.config

import com.example.recipeinstructions.model.ApiResponse
import retrofit2.Response


abstract class NetworkBoundRepository<apiResponse> {

    suspend fun callBack(onCallBack: onCallBack<apiResponse>) {
        val apiResponse = fetchFromRemote()
        val remotePosts = apiResponse.body()
        if (apiResponse.isSuccessful && remotePosts != null) {
            if (remotePosts?.success == true && remotePosts.data != null) {
                onCallBack.success(remotePosts.data)
            } else {
                onCallBack.onErr(remotePosts.message)
            }
        } else onCallBack.onErr(apiResponse.message())


    }

    protected abstract suspend fun fetchFromRemote(): Response<ApiResponse<apiResponse>>
}

interface onCallBack<T> {
    fun success(data: T)
    fun onErr(message: String)
}