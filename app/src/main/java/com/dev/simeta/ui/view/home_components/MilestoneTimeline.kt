package com.dev.simeta.ui.view.home_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define data class to represent each milestone item
data class MilestoneItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val status: MilestoneStatus
)

// Enum to represent the status of each milestone
enum class MilestoneStatus {
    COMPLETED,
    IN_PROGRESS,
    PENDING
}

@Composable
fun MilestoneTimeline(
    modifier: Modifier = Modifier
) {
    val milestones = listOf(
        MilestoneItem(
            "Pengajuan Judul TA",
            "Anda telah menyelesaikan Pengajuan Judul TA, lanjutkan tahap selanjutnya",
            Icons.Default.Description,
            MilestoneStatus.COMPLETED
        ),
        MilestoneItem(
            "Pengajuan Proposal TA",
            "Anda sedang Mengajukan Proposal TA, ingat timeline dan lengkapi dokumen",
            Icons.Default.Description,
            MilestoneStatus.IN_PROGRESS
        ),
        MilestoneItem(
            "Seminar Hasil TA",
            "Waduh anda belum sampai tahap ini",
            Icons.Default.PresentToAll,
            MilestoneStatus.PENDING
        ),
        MilestoneItem(
            "Sidang TA",
            "Waduh anda belum sampai tahap ini",
            Icons.Default.PresentToAll,
            MilestoneStatus.PENDING
        ),
        MilestoneItem(
            "Kelulusan",
            "Waduh anda belum sampai tahap ini",
            Icons.Default.School,
            MilestoneStatus.PENDING
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Reduced padding to minimize extra spacing
        ) {
            Text(
                text = "Milestone TA",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp) // Reduced bottom padding for compact spacing
            )

            milestones.forEachIndexed { index, milestone ->
                MilestoneItemView(
                    milestone = milestone,
                    isLast = index == milestones.lastIndex
                )
            }
        }
    }
}

@Composable
private fun MilestoneItemView(
    milestone: MilestoneItem,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    val statusColor = when (milestone.status) {
        MilestoneStatus.COMPLETED -> Color(0xFF00C853)
        MilestoneStatus.IN_PROGRESS -> Color(0xFFFFB300)
        MilestoneStatus.PENDING -> Color(0xFFBDBDBD)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp) // Reduced vertical padding for more compact spacing
    ) {
        // Timeline dot and line representation
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp) // Dot size for milestone
                    .clip(CircleShape)
                    .background(statusColor)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(64.dp) // Adjusted height to ensure continuous line between dots
                        .background(statusColor) // Use the same color as the status for better continuity
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp)) // Reduced space between timeline and content

        // Enhanced Content Card for milestone details
        Row(
            verticalAlignment = Alignment.Top, // Align to the top for a consistent start alignment
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            Icon(
                imageVector = milestone.icon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(32.dp) // Icon only without a separate container
            )

            Spacer(modifier = Modifier.width(8.dp)) // Reduced space between icon and text

            Column(
                verticalArrangement = Arrangement.Top // Align contents of column to the top
            ) {
                Text(
                    text = milestone.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp)) // Reduced spacer for more compact appearance
                Text(
                    text = milestone.description,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
        }
    }
}
