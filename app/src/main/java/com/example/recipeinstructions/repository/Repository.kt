package com.example.recipeinstructions.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.recipeinstructions.local.AppDatabase
import com.example.recipeinstructions.model.ApiResponse
import com.example.recipeinstructions.model.Beverage
import com.example.recipeinstructions.model.Favourite
import com.example.recipeinstructions.model.Instruction
import com.example.recipeinstructions.model.TypeRequest
import com.example.recipeinstructions.network.ApiRepository
import com.example.recipeinstructions.network.RetrofitBuilder
import com.example.recipeinstructions.network.config.NetworkBoundRepository
import com.example.recipeinstructions.network.config.onCallBack
import retrofit2.Response

class Repository(application: Application) {
    private val apiRepository = RetrofitBuilder(application).getRetrofit()
    private val database = AppDatabase.getInstance(application)
    suspend fun getTypeDrink(onCallBack: onCallBack<ArrayList<Beverage>>) {
        return object : NetworkBoundRepository<ArrayList<Beverage>>() {
            override suspend fun fetchFromRemote(): Response<ApiResponse<ArrayList<Beverage>>> {
                return apiRepository.getTypeDrink()
            }

        }.callBack(onCallBack)
    }

    suspend fun getDrink(
        typeRequest: TypeRequest,
        onCallBack: onCallBack<ArrayList<Instruction>>
    ) {
        return object : NetworkBoundRepository<ArrayList<Instruction>>() {
            override suspend fun fetchFromRemote(): Response<ApiResponse<ArrayList<Instruction>>> {
                return apiRepository.getDrink(typeRequest)
            }

        }.callBack(onCallBack)
    }

    suspend fun searchDrink(
        key: String,
        onCallBack: onCallBack<ArrayList<Instruction>>
    ) {
        return object : NetworkBoundRepository<ArrayList<Instruction>>() {
            override suspend fun fetchFromRemote(): Response<ApiResponse<ArrayList<Instruction>>> {
                return apiRepository.searchDrink(key)
            }

        }.callBack(onCallBack)
    }

    suspend fun detailDrink(
        key: String,
        onCallBack: onCallBack<Instruction>,
    ) {
        return object : NetworkBoundRepository<Instruction>() {
            override suspend fun fetchFromRemote(): Response<ApiResponse<Instruction>> {
                return apiRepository.detailDrink(key)
            }

        }.callBack(onCallBack)
    }

    companion object {
        private var instance: Repository? = null
        fun getInstance(application: Application): Repository {
            if (instance == null) {
                instance = Repository(application)
            }
            return instance ?: Repository(application)
        }
    }

    fun getLiveData(): LiveData<List<Favourite>> = database.getSpendingDao().getAll()
    fun remove(item: Favourite) = database.getSpendingDao().delete(item)
    fun add(item: Favourite) = database.getSpendingDao().insertAll(item)
    fun getAllData() = database.getSpendingDao().getAllData()
}