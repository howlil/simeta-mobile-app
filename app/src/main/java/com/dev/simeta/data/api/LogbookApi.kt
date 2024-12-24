package com.dev.simeta.data.api

import com.dev.simeta.data.model.CreateLogbookResponse
import com.dev.simeta.data.model.DeleteLogbookResponse
import com.dev.simeta.data.model.DetailLogBookResponse
import com.dev.simeta.data.model.LogbookResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface LogbookApi {
    @GET("api/v1/logbooks")
    suspend fun getLogbooks(
        @Header("Authorization") token: String
    ): LogbookResponse

    @GET("api/v1/logbooks/{id}")
    suspend fun getLogbookDetail(
        @Header("Authorization") token: String,
        @Path("id") logbookId: String
    ): DetailLogBookResponse

    @DELETE("api/v1/logbook/{id}")
    suspend fun deleteLogbook(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DeleteLogbookResponse

    @Multipart
    @POST("api/v1/logbook")
    suspend fun createLogbook(
        @Header("Authorization") token: String,
        @Part("date") date: RequestBody,
        @Part("activity") activity: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part attachment_url: MultipartBody.Part?
    ): CreateLogbookResponse
}
