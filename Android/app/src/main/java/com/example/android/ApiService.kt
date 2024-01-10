package com.example.android

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun login(@Body requestBody: LoginRequest): LoginResponse

    @GET("/me")
    suspend fun getUserData(@Query("user_id") userId: Int): UserData

    @GET("/search")
    suspend fun searchLog(
        @Query("user_id") userId: Int,
        @Query("content") content: String
    ): List<LogData>

    @GET("/log")
    suspend fun getLog(
        @Query("user_id") userId: Int
    ): List<LogData>

    @POST("/dish")
    suspend fun postDishData(@Body dishes: List<Dish>): DishStatisticsResponse
}
