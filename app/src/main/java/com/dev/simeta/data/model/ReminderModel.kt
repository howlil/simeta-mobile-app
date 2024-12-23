package com.dev.simeta.data.model


data class ReminderResponse(
    val error: Boolean,
    val messages: String,
    val data: List<Reminder>
)

data class Reminder(
    val id: String,
    val title: String,
    val message: String,
    val due_date: String,
    val mahasiswa_id: String,
    val created_at: String,
    val updated_at: String
)

data class CreateReminderRequest(
    val title: String,
    val message: String,
    val due_date: String
)

data class CreateReminderResponse(
    val error: Boolean,
    val messages: String,
    val data: ReminderData
)

data class ReminderData(
    val id: String,
    val title: String,
    val message: String,
    val due_date: String,
    val created_at: String,
    val updated_at: String
)