package com.dev.simeta.ui.view.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dev.simeta.ui.view.home.home_components.MilestoneCard
import com.dev.simeta.ui.view.home.home_components.MilestoneTimeline
import com.dev.simeta.ui.view.home.home_components.NavigationMenu
import com.dev.simeta.ui.view.home.home_components.ProfileHeader

@Composable
fun HomeScreen(navController: NavController) { // Add NavController as a parameter
    val scrollState = rememberScrollState()
    val context = LocalContext.current // Get the current context

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        ProfileHeader(
            name = "Mhd Ulil Abshar",
            email = "2211521003_ulil@student.ac.id"
        )

        Spacer(modifier = Modifier.height(24.dp))

        MilestoneCard(
            title = "Kerjakan Milestone",
            date = "20 Oktober 2024",
            logbookCount = "10/16",
            milestone = "Seminar TA",
            status = "Pending"
        )
        Spacer(modifier = Modifier.height(12.dp))

        NavigationMenu(navController = navController) // Pass NavController to NavigationMenu

        Spacer(modifier = Modifier.height(12.dp))

        MilestoneTimeline(context = context)
    }
}
