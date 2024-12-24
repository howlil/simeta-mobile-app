package com.dev.simeta.data.api

import com.dev.simeta.data.model.DashboardResponse
import com.dev.simeta.data.model.LoginRequest
import com.dev.simeta.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("/api/v1/dashboard")
    suspend fun getDashboard(@Header("Authorization") token: String): DashboardResponse
}