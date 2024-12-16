package com.dev.simeta.ui.view
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dev.simeta.ui.view.home.home_components.MilestoneCard
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.dev.simeta.ui.view.home.home_components.MilestoneTimeline
import com.dev.simeta.ui.view.home.home_components.NavigationMenu
import com.dev.simeta.ui.view.home.home_components.ProfileHeader

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

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

        NavigationMenu()

        Spacer(modifier = Modifier.height(12.dp))

        MilestoneTimeline()
    }
}
