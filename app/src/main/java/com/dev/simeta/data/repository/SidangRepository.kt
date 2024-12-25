package com.dev.simeta.data.repository

import com.dev.simeta.data.api.SidangApi
import com.dev.simeta.data.model.SidangResponse
import com.dev.simeta.data.model.SidangResponseCreate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class SidangRepository(private val sidangApi: SidangApi) {
    suspend fun getSidang(token: String): Result<SidangResponse> {
        return try {
            val response = sidangApi.getSidang("Bearer $token")
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Unknown error"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }

    suspend fun createSidang(
        token: String,
        date: RequestBody,
        time: RequestBody,
        dokumen: MultipartBody.Part
    ): Result<SidangResponseCreate> {
        return try {
            val response = sidangApi.createSidang("Bearer $token", date, time, dokumen)
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
