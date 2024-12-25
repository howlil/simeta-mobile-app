package com.dev.simeta.data.api

import com.dev.simeta.data.model.DashboardResponse
import com.dev.simeta.data.model.LoginRequest
import com.dev.simeta.data.model.LoginResponse
import com.dev.simeta.data.model.UpdateMahasiswaResponse
import com.dev.simeta.data.model.UserResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {
    @POST("api/v1/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/v1/dashboard")
    suspend fun getDashboard(@Header("Authorization") token: String): DashboardResponse

    @GET("api/v1/me")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): UserResponse

    @PATCH("api/v1/me")
    suspend fun updateMahasiswa(
        @Header("Authorization") token: String,
        @Body request: Map<String, @JvmSuppressWildcards RequestBody>
    ): UpdateMahasiswaResponse
}