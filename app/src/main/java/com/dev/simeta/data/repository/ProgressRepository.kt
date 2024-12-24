package com.dev.simeta.data.repository

import com.dev.simeta.data.api.ProgressApi
import com.dev.simeta.data.model.CreateProgressRequest
import com.dev.simeta.data.model.CreateProgressResponse
import com.dev.simeta.data.model.ProgressResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProgressRepository @Inject constructor(
    private val progressApi: ProgressApi
) {
    suspend fun getProgress(token: String, milestoneId: String? = null): Result<ProgressResponse> {
        return try {
            val response = progressApi.getProgress("Bearer $token", milestoneId)
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception(errorBody ?: "Unknown server error"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
    suspend fun createProgress(token: String, request: CreateProgressRequest): Result<CreateProgressResponse> {
        return try {
            val response = progressApi.createProgress("Bearer $token", request)
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(Exception(errorBody ?: "Unknown server error"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }

}
