package com.dev.simeta.ui.view.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.ui.view.home.home_components.DashboardViewModel
import com.dev.simeta.ui.view.home.home_components.MilestoneCard
import com.dev.simeta.ui.view.home.home_components.MilestoneTimeline
import com.dev.simeta.ui.view.home.home_components.NavigationMenu
import com.dev.simeta.ui.view.home.home_components.ProfileHeader

@Composable
fun HomeScreen(navController: NavController,viewModel: DashboardViewModel = hiltViewModel()) { // Add NavController as a parameter
    val context = LocalContext.current // Get the current context
    val dashboardState = viewModel.dashboardState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchDashboardData(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        ProfileHeader(
            name = "Mhd Ulil Abshar",
            email = "2211521003_ulil@student.ac.id"
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = dashboardState) {
            is DashboardViewModel.DashboardState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is DashboardViewModel.DashboardState.Success -> {
                val data = state.data
                val reminderTitle = data.reminder?.title ?: "No Reminders"
                val reminderDate = data.reminder?.due_date ?: "N/A"

                val currentMilestoneName = data.current_milestone?.name ?: "No Current Milestone"
                val currentMilestoneStatus = data.current_milestone?.status ?: "N/A"

                MilestoneCard(
                    title = reminderTitle,
                    date = reminderDate,
                    logbookCount = "${data.logbook.count}/${data.logbook.max}",
                    milestone = currentMilestoneName,
                    status = currentMilestoneStatus
                )

                Spacer(modifier = Modifier.height(12.dp))
                NavigationMenu(navController = navController)

                Spacer(modifier = Modifier.height(12.dp))
                MilestoneTimeline(context = context)
            }
            is DashboardViewModel.DashboardState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            DashboardViewModel.DashboardState.Idle -> {
                // Idle State: No UI shown
            }
        }

    }
}
