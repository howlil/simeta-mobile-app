package com.dev.simeta.data.repository

import com.dev.simeta.data.api.MilestoneApi
import com.dev.simeta.data.model.MilestoneResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MilestoneRepository @Inject constructor(
    private val milestoneApi: MilestoneApi
) {
    suspend fun getMilestones(token: String): Result<MilestoneResponse> {
        return try {
            val response = milestoneApi.getMilestones("Bearer $token")
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
