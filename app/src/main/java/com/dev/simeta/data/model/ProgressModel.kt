package com.dev.simeta.data.model

data class ProgressResponse(
    val error: Boolean,
    val messages: String,
    val data: List<Progress>
)

data class Progress(
    val id: String,
    val title: String,
    val details: String,
    val created_at: String,
    val updated_at: String,
    val milestones: List<MilestoneProgress>
)

data class MilestoneProgress(
    val milestone_id: String,
    val milestone_name: String,
    val milestone_description: String,
    val milestone_status: String,
    val point: Int,
    val max_point: Int
)

data class CreateProgressRequest(
    val milestone_id: String,
    val title: String,
    val details: String
)

data class CreateProgressResponse(
    val error: Boolean,
    val messages: String,
    val data: ProgressCreationData // Changed the conflicting name
)

data class ProgressCreationData( // Renamed from ProgressData to avoid conflict
    val progress: ProgressDetails,
    val milestone: MilestoneDetails
)

data class ProgressDetails(
    val id: String,
    val title: String,
    val details: String,
    val created_at: String,
    val updated_at: String
)

data class MilestoneDetails(
    val id: String,
    val status: String,
    val total_points: Int
)
