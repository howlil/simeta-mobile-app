package com.dev.simeta.data.repository

import android.content.Context
import android.util.Log
import com.dev.simeta.data.api.AuthApi
import com.dev.simeta.data.model.DashboardResponse
import com.dev.simeta.data.model.LoginRequest
import com.dev.simeta.data.model.LoginResponse
import com.dev.simeta.data.model.UpdateMahasiswaResponse
import com.dev.simeta.data.model.UserResponse
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    suspend fun getUser(token: String): Result<UserResponse> {
        return try {
            val response = authApi.getUser("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
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

    suspend fun updateMahasiswa(
        token: String,
        fullName: String? = null,
        email: String? = null,
    ): Result<UpdateMahasiswaResponse> {
        return try {
            // Logging input parameters
            Log.d("AuthRepository", "Update Mahasiswa: Token: Bearer $token")
            Log.d("AuthRepository", "Update Mahasiswa: Full Name: $fullName, Email: $email")

            // Build request body map
            val requestBody = mutableMapOf<String, RequestBody>()
            fullName?.let {
                Log.d("AuthRepository", "Adding full_name to request body: $it")
                requestBody["full_name"] = it.toRequestBody("text/plain".toMediaTypeOrNull())
            }
            email?.let {
                Log.d("AuthRepository", "Adding email to request body: $it")
                requestBody["email"] = it.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val response = authApi.updateMahasiswa("Bearer $token", requestBody)

            // Log successful response
            Log.d("AuthRepository", "Update Mahasiswa Success: $response")
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthRepository", "HTTP error updating mahasiswa: $errorBody")
            Result.failure(Exception(errorBody ?: "Unknown HTTP error"))
        } catch (e: IOException) {
            Log.e("AuthRepository", "Network error updating mahasiswa: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Unexpected error updating mahasiswa: ${e.message}")
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }

    suspend fun updatePassword(
        token: String,
        password: String? = null,
    ): Result<UpdateMahasiswaResponse> {
        return try {
            // Logging input parameters
            Log.d("AuthRepository", "Update Password: Token: Bearer $token")
            Log.d("AuthRepository", "Update Password: Password: $password")

            // Build request body map
            val requestBody = mutableMapOf<String, RequestBody>()
            password?.let {
                Log.d("AuthRepository", "Adding password to request body: $it")
                requestBody["password"] = it.toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val response = authApi.updateMahasiswa("Bearer $token", requestBody)

            // Log successful response
            Log.d("AuthRepository", "Update Password Success: $response")
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthRepository", "HTTP error updating password: $errorBody")
            Result.failure(Exception(errorBody ?: "Unknown HTTP error"))
        } catch (e: IOException) {
            Log.e("AuthRepository", "Network error updating password: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Unexpected error updating password: ${e.message}")
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }



}
