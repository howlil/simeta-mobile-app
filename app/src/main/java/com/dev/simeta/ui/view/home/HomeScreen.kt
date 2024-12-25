package com.dev.simeta.ui.view.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.dev.simeta.ui.view.home.home_components.UserViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dashboardState = dashboardViewModel.dashboardState.collectAsState().value
    val userState = userViewModel.userState.collectAsState().value

    // Fetch data when the screen loads
    LaunchedEffect(Unit) {
        userViewModel.fetchUserData(context)
        dashboardViewModel.fetchDashboardData(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Handle ProfileHeader state
        when (userState) {
            is UserViewModel.UserState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UserViewModel.UserState.Success -> {
                ProfileHeader(
                    name = userState.data.full_name,
                    email = userState.data.email
                )
            }
            is UserViewModel.UserState.Error -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Failed to load user data: ${userState.message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            UserViewModel.UserState.Idle -> {
                // Tambahkan UI jika tidak ada tindakan yang dilakukan pada state Idle
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No action performed yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Handle Dashboard state
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
                MilestoneCard(
                    title = data.reminder?.title ?: "No Reminders",
                    date = data.reminder?.due_date ?: "N/A",
                    logbookCount = "${data.logbook.count}/${data.logbook.max}",
                    milestone = data.current_milestone?.name ?: "No Current Milestone",
                    status = data.current_milestone?.status ?: "N/A"
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
                // No UI displayed in the Idle state
            }
        }
    }
}
