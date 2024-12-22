package com.dev.simeta.data.repository

import com.dev.simeta.data.api.AuthApi
import com.dev.simeta.data.model.LoginRequest
import com.dev.simeta.data.model.LoginResponse
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(private val authApi: AuthApi) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            android.util.Log.d("AuthRepository", "Sending request with email=$email, password=$password")
            val response = authApi.login(request)
            android.util.Log.d("AuthRepository", "Response received: $response")
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("AuthRepository", "HTTP error: $errorBody")
            Result.failure(Exception(errorBody ?: "Unknown server error"))
        } catch (e: IOException) {
            android.util.Log.e("AuthRepository", "Network error: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Unexpected error: ${e.message}")
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
}
