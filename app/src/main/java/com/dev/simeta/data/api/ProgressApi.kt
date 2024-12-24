package com.dev.simeta.data.api

import com.dev.simeta.data.model.CreateProgressRequest
import com.dev.simeta.data.model.CreateProgressResponse
import com.dev.simeta.data.model.ProgressResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ProgressApi {
    @POST("api/v1/progress")
    suspend fun createProgress(
        @Header("Authorization") token: String,
        @Body request: CreateProgressRequest
    ): CreateProgressResponse

    @GET("api/v1/progress")
    suspend fun getProgress(
        @Header("Authorization") token: String,
        @Query("milestone_id") milestoneId: String? = null
    ): ProgressResponse

}
