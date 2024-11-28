package com.dev.simeta.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import com.dev.simeta.data.model.LoginRequest
import com.dev.simeta.data.model.LoginResponse

interface AuthApi {
    @POST("v1/login")
    suspend fun login(@Body request: LoginRequest) : LoginResponse
}