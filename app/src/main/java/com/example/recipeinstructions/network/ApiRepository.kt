package com.example.recipeinstructions.network

import com.example.recipeinstructions.model.ApiResponse
import com.example.recipeinstructions.model.Beverage
import com.example.recipeinstructions.model.Instruction
import com.example.recipeinstructions.model.TypeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiRepository {

    @GET(ApiPath.URL_TYPE_DRINK)
    suspend fun getTypeDrink(): Response<ApiResponse<ArrayList<Beverage>>>

    @POST(ApiPath.URL_DRINK)
    suspend fun getDrink(@Body typeRequest: TypeRequest): Response<ApiResponse<ArrayList<Instruction>>>

    @GET(ApiPath.URL_SEARCH_DRINK)
    suspend fun searchDrink(@Query("q") key: String): Response<ApiResponse<ArrayList<Instruction>>>

    @GET(ApiPath.URL_DETAIL_DRINK)
    suspend fun detailDrink(@Query("id") key: String): Response<ApiResponse<Instruction>>

}
