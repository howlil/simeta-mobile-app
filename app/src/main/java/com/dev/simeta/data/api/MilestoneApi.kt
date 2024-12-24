package com.dev.simeta.data.api

import com.dev.simeta.data.model.MilestoneResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface MilestoneApi {
    @GET("api/v1/milestones")
    suspend fun getMilestones(
        @Header("Authorization") token: String
    ): MilestoneResponse
}
