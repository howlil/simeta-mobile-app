package com.dev.simeta.ui.view.home.home_components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.simeta.data.model.Milestone
import com.dev.simeta.ui.view.home.MilestoneTimelineViewModel

@Composable
fun MilestoneTimeline(
    context: Context,
    viewModel: MilestoneTimelineViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val milestoneState by viewModel.milestoneState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMilestones(context)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        when (milestoneState) {
            is MilestoneTimelineViewModel.MilestoneState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is MilestoneTimelineViewModel.MilestoneState.Success -> {
                val milestones = (milestoneState as MilestoneTimelineViewModel.MilestoneState.Success).milestones
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Milestone TA",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    milestones.forEachIndexed { index, milestone ->
                        MilestoneItemView(
                            milestone = milestone,
                            isLast = index == milestones.lastIndex
                        )
                    }
                }
            }
            is MilestoneTimelineViewModel.MilestoneState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (milestoneState as MilestoneTimelineViewModel.MilestoneState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            MilestoneTimelineViewModel.MilestoneState.Idle -> Unit
            else -> {null}
        }
    }
}

@Composable
private fun MilestoneItemView(
    milestone: Milestone,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    val statusColor = when (milestone.status) {
        "COMPLETED" -> Color(0xFF00C853)
        "IN_PROGRESS" -> Color(0xFFFFB300)
        else -> Color(0xFFBDBDBD)
    }

    val iconRes = when (milestone.name) {
        "Pengajuan Judul TA" -> Icons.Default.Add
        "Pengajuan Proposal TA" -> Icons.Default.Description
        "Seminar Hasil TA" -> Icons.Default.Assignment
        "Sidang TA" -> Icons.Default.Tv
        "Kelulusan" -> Icons.Default.School
        else -> Icons.Default.Circle
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(48.dp)
                        .background(statusColor)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    when (milestone.status) {
                        "COMPLETED" -> Color(0xFFE8F5E9)
                        "IN_PROGRESS" -> Color(0xFFFFF8E1)
                        else -> Color(0xFFF5F5F5)
                    }
                )
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (milestone.status) {
                            "COMPLETED" -> Color(0xFFA5D6A7)
                            "IN_PROGRESS" -> Color(0xFFFFE082)
                            else -> Color(0xFFE0E0E0)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconRes,
                    contentDescription = milestone.name,
                    tint = when (milestone.status) {
                        "COMPLETED" -> Color(0xFF2E7D32)
                        "IN_PROGRESS" -> Color(0xFFF57F17)
                        else -> Color(0xFF757575)
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = milestone.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = milestone.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    lineHeight = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}