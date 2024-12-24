package com.dev.simeta.data.model

data class MilestoneResponse(
    val error: Boolean,
    val messages: String,
    val data: List<Milestone>
)

data class Milestone(
    val id: String,
    val name: String,
    val description: String,
    val status: String,
    val max_point: Int,
    val created_at: String,
    val updated_at: String,
    val progress: List<ProgressData>
)

data class ProgressData(
    val progress_id: String,
    val title: String,
    val details: String,
    val point: Int
)
