package com.dev.simeta.data.repository

import android.content.Context
import com.dev.simeta.data.api.AuthApi
import com.dev.simeta.data.model.DashboardResponse
import com.dev.simeta.data.model.LoginRequest
import com.dev.simeta.data.model.LoginResponse
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val authApi: AuthApi

) {

    suspend fun login(email: String, password: String, context: Context): Result<LoginResponse> {
        return try {
            val fcmToken = getFcmToken() // Get FCM token
            if (fcmToken == null) {
                return Result.failure(Exception("Unable to retrieve FCM Token"))
            }

            val request = LoginRequest(email, password, fcmToken)
            android.util.Log.d("AuthRepository", "Sending request with email=$email, fcmToken=$fcmToken")
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

    private suspend fun getFcmToken(): String? {
        return try {
            com.google.firebase.messaging.FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Error retrieving FCM token: ${e.message}")
            null
        }
    }


    suspend fun getDashboard(token: String): Result<DashboardResponse> {
        return try {
            val response = authApi.getDashboard("Bearer $token")
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Unknown error"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
}
