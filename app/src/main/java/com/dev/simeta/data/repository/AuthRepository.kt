package com.dev.simeta.data.repository

import com.dev.simeta.data.api.AuthApi
import com.dev.simeta.data.model.LoginRequest
import com.dev.simeta.data.model.LoginResponse
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(private val authApi: AuthApi) {

    /**
     * Fungsi untuk melakukan login
     * @param email: String
     * @param password: String
     * @return LoginResponse
     */
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = authApi.login(request)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception("Terjadi kesalahan pada server: ${e.message}"))
        } catch (e: IOException) {
            Result.failure(Exception("Tidak dapat terhubung ke server. Periksa koneksi internet Anda."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
