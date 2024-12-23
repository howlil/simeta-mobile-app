package com.dev.simeta.data.repository

import com.dev.simeta.data.api.ReminderApi
import com.dev.simeta.data.model.CreateReminderRequest
import com.dev.simeta.data.model.CreateReminderResponse
import com.dev.simeta.data.model.ReminderResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderApi: ReminderApi
) {
    suspend fun getReminders(token: String): Result<ReminderResponse> {
        return try {
            val response = reminderApi.getReminders("Bearer $token")
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
    suspend fun createReminder(token: String, request: CreateReminderRequest): Result<CreateReminderResponse> {
        return try {
            val response = reminderApi.createReminder("Bearer $token", request)
            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(Exception(e.response()?.errorBody()?.string() ?: "Unknown server error"))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
}
