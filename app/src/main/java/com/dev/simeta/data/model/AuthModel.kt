package com.dev.simeta.data.model

data class LoginRequest(
    val email: String,
    val password: String,
    val fcmToken: String
)

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val data: Token?
)

data class Token(
    val token: String
)


data class DashboardResponse(
    val error: Boolean,
    val messages: String,
    val data: DashboardData
)

data class DashboardData(
    val reminder: ReminderDasboard?,
    val logbook: LogbookStats,
    val current_milestone: CurrentMilestone,
    val milestones: List<MilestoneDashboard>
)

data class ReminderDasboard(
    val id: String,
    val title: String,
    val message: String,
    val due_date: String
)

data class LogbookStats(
    val count: Int,
    val max: Int
)

data class CurrentMilestone(
    val id: String,
    val name: String,
    val description: String,
    val status: String
)

data class MilestoneDashboard(
    val id: String,
    val name: String,
    val status: String
)

data class UserResponse(
    val error: Boolean,
    val message: String,
    val data: UserData
)

data class UserData(
    val id: String,
    val full_name: String,
    val email: String,
    val nim: String,
    val photoUrl: String?,
    val fcmToken: String?,
    val created_at: String,
    val updated_at: String
)

data class UpdateMahasiswaResponse(
    val error: Boolean,
    val messages: String,
    val data: Mahasiswa
)

data class Mahasiswa(
    val id: String,
    val full_name: String,
    val email: String,
    val nim: String,
    val photoUrl: String?,
    val password: String,
    val fcmToken: String?,
    val created_at: String,
    val updated_at: String
)

