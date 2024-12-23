package com.dev.simeta.data.api

import com.dev.simeta.data.model.CreateReminderRequest
import com.dev.simeta.data.model.CreateReminderResponse
import com.dev.simeta.data.model.ReminderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
interface ReminderApi {
    @GET("api/v1/reminders")
    suspend fun getReminders(
        @Header("Authorization") token: String
    ): ReminderResponse

    @POST("api/v1/reminder")
    suspend fun createReminder(
        @Header("Authorization") token: String,
        @Body request: CreateReminderRequest
    ): CreateReminderResponse

}
