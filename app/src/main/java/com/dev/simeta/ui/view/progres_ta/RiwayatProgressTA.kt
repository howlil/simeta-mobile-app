package com.dev.simeta.ui.view.progress_ta

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.data.model.Milestone
import com.dev.simeta.data.model.Progress
import com.dev.simeta.ui.components.DynamicTopBar
import com.dev.simeta.ui.components.ErrorView

@Composable
fun RiwayatProgressTA(
    navController: NavController,
    context: Context,
    viewModel: RiwayatProgressViewModel = hiltViewModel()
) {
    val milestoneState by viewModel.milestoneState.collectAsState()
    val progressState by viewModel.progressState.collectAsState()

    var selectedMilestoneId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchMilestones(context)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar
        DynamicTopBar(
            title = "Progress TA",
            showBackButton = true,
            showAddButton = true,
            onAddClick = { navController.navigate("form_progress") },
            onBackClick = { navController.popBackStack() },
        )

        // Filter Chips
        when (milestoneState) {
            is RiwayatProgressViewModel.MilestoneState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RiwayatProgressViewModel.MilestoneState.Success -> {
                val milestones = (milestoneState as RiwayatProgressViewModel.MilestoneState.Success).milestones
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(milestones) { milestone ->
                        MilestoneFilterChip(
                            milestone = milestone,
                            isSelected = milestone.id == selectedMilestoneId,
                            onSelect = {
                                selectedMilestoneId = if (selectedMilestoneId == milestone.id) null else milestone.id
                                viewModel.fetchProgress(context, selectedMilestoneId)
                            }
                        )
                    }
                }
            }
            is RiwayatProgressViewModel.MilestoneState.Error -> {
                ErrorView(
                    message = (milestoneState as RiwayatProgressViewModel.MilestoneState.Error).message,
                    onRetry = { viewModel.fetchMilestones(context) }
                )
            }
            RiwayatProgressViewModel.MilestoneState.Idle -> Unit
        }

        // Progress Content
        when (progressState) {
            is RiwayatProgressViewModel.ProgressState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RiwayatProgressViewModel.ProgressState.Success -> {
                val progressList =
                    (progressState as RiwayatProgressViewModel.ProgressState.Success).progressList
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(progressList) { progress ->
                        ProgressCard(progress)
                    }
                }
            }
            is RiwayatProgressViewModel.ProgressState.Error -> {
                ErrorView(
                    message = (progressState as RiwayatProgressViewModel.ProgressState.Error).message,
                    onRetry = { viewModel.fetchProgress(context, selectedMilestoneId) }
                )
            }
            RiwayatProgressViewModel.ProgressState.Idle -> Unit
        }
    }
}

@Composable
fun MilestoneFilterChip(
    milestone: Milestone,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(backgroundColor, MaterialTheme.shapes.small)
            .border(width = if (isSelected) 0.dp else 1.dp, color = borderColor, shape = MaterialTheme.shapes.small)
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = milestone.name,
            color = contentColor,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ProgressCard(progress: Progress) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(16.dp) // Inner padding for content
    ) {
        Column {
            Text(
                text = progress.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = progress.details,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
