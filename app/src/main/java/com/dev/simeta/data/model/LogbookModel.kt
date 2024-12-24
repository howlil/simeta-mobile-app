package com.dev.simeta.data.model

data class LogbookResponse(
    val error: Boolean,
    val messages: String,
    val data: List<Logbook>
)
data class Logbook(
    val id: String,
    val date: String,
    val activity: String,
    val notes: String,
    val attachment_url: String,
    val ta_id: String,
    val created_at: String,
    val updated_at: String
)

data class DetailLogBookResponse(
    val error: Boolean,
    val messages: String,
    val data: DetailLogBook
)

data class DetailLogBook(
    val id: String,
    val date: String,
    val activity: String,
    val notes: String,
    val attachment_url: String,
    val ta_id: String,
)
data class DeleteLogbookResponse(
    val error: Boolean,
    val messages: String
)

data class CreateLogbookResponse(
    val error: Boolean,
    val messages: String,
    val data: LogbookData
)
data class LogbookData(
    val id: String,
    val date: String,
    val activity: String,
    val notes: String,
    val attachment_url: String?,
    val ta_id: String,
    val created_at: String,
    val updated_at: String
)