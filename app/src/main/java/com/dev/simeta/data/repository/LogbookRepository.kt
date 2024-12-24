package com.dev.simeta.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.dev.simeta.data.api.LogbookApi
import com.dev.simeta.data.model.CreateLogbookResponse
import com.dev.simeta.data.model.DeleteLogbookResponse
import com.dev.simeta.data.model.DetailLogBook
import com.dev.simeta.data.model.LogbookResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

class LogbookRepository(private val logbookApi: LogbookApi) {

    suspend fun getLogbooks(token: String): Result<LogbookResponse> {
        return try {
            android.util.Log.d("LogbookRepository", "Sending token: Bearer $token")
            val response = logbookApi.getLogbooks("Bearer $token")
            android.util.Log.d("LogbookRepository", "Response: $response")
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("LogbookRepository", "HTTP error: $errorBody")
            Result.failure(Exception(errorBody ?: "Unknown server error"))
        } catch (e: IOException) {
            android.util.Log.e("LogbookRepository", "Network error: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            android.util.Log.e("LogbookRepository", "Unexpected error: ${e.message}")
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }

    suspend fun getLogbookDetail(token: String, logbookId: String): Result<DetailLogBook> {
        return try {
            val response = logbookApi.getLogbookDetail("Bearer $token", logbookId)
            Result.success(response.data) // Hanya mengembalikan objek DetailLogBook
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown server error"
            Result.failure(Exception(errorMessage))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: Please check your connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }
    suspend fun deleteLogbook(token:String,id: String): Result<DeleteLogbookResponse> {
        return try {
            val response = logbookApi.deleteLogbook("Bearer $token", id)
            Result.success(response) // Hanya mengembalikan objek DetailLogBook
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown server error"
            Result.failure(Exception(errorMessage))
        } catch (e: IOException) {
            Result.failure(Exception("Network error: Please check your connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }

    suspend fun createLogbook(
        token: String,
        date: String,
        activity: String,
        notes: String,
        attachment: Uri?,
        context: Context
    ): Result<CreateLogbookResponse> {
        android.util.Log.d("LogbookRepository", "Starting createLogbook with token: Bearer $token, date: $date, activity: $activity, notes: $notes")
        return try {
            val dateBody = date.toRequestBody("text/plain".toMediaType())
            val activityBody = activity.toRequestBody("text/plain".toMediaType())
            val notesBody = notes.toRequestBody("text/plain".toMediaType())

            val attachmentPart = attachment?.let {
                android.util.Log.d("LogbookRepository", "Preparing attachment with URI: $attachment")
                val stream = context.contentResolver.openInputStream(it)
                val fileBytes = stream?.readBytes()
                stream?.close()

                val fileName = getFileName(context, it)
                android.util.Log.d("LogbookRepository", "Attachment fileName: $fileName")

                val requestFile = fileBytes?.toRequestBody(
                    context.contentResolver.getType(it)?.toMediaType()
                        ?: "application/octet-stream".toMediaType()
                )

                requestFile?.let {
                    MultipartBody.Part.createFormData("attachment_url", fileName, it) // Ubah menjadi `attachment_url`
                }
            }

            val response = logbookApi.createLogbook(
                "Bearer $token",
                dateBody,
                activityBody,
                notesBody,
                attachmentPart
            )
            android.util.Log.d("LogbookRepository", "createLogbook Response: $response")
            Result.success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("LogbookRepository", "createLogbook HTTP error: $errorBody")
            Result.failure(Exception(errorBody ?: "Unknown server error"))
        } catch (e: IOException) {
            android.util.Log.e("LogbookRepository", "createLogbook Network error: ${e.message}")
            Result.failure(Exception("Network error: ${e.message}"))
        } catch (e: Exception) {
            android.util.Log.e("LogbookRepository", "createLogbook Unexpected error: ${e.message}")
            Result.failure(Exception("Unexpected error: ${e.message}"))
        }
    }


    private fun getFileName(context: Context, uri: Uri): String {
        android.util.Log.d("LogbookRepository", "Getting file name for URI: $uri")
        var fileName = "file"
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        android.util.Log.d("LogbookRepository", "File name: $fileName")
        return fileName
    }
}

