package com.dev.simeta.data.repository

import android.util.Log
import com.dev.simeta.data.api.AuthApi
import com.dev.simeta.data.model.LoginRequest
import com.dev.simeta.data.model.LoginResponse
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(private val authApi: AuthApi) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = authApi.login(request)
            Result.success(response)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown server error"
            Result.failure(Exception(errorMessage))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: Please check your connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
}

