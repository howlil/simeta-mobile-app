package com.dev.simeta.ui.view.home.home_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun NavigationMenu(
    navController: NavController, // Pass the NavController here
    modifier: Modifier = Modifier
) {
    val navigationItems = listOf(
        NavigationItem(
            title = "Judul TA",
            icon = Icons.Outlined.Add,
            route = "judulta" // Ensure this route matches the one in NavHost
        ),
        NavigationItem(
            title = "Proposal",
            icon = Icons.Outlined.Description,
            route = "proposal"
        ),
        NavigationItem(
            title = "Semhas",
            icon = Icons.Outlined.Assignment,
            route = "semhas"
        ),
        NavigationItem(
            title = "Sidang",
            icon = Icons.Outlined.Description,
            route = "sidang"
        ),
        NavigationItem(
            title = "Reminder",
            icon = Icons.Outlined.Timelapse,
            route = "reminder"
        ),
        NavigationItem(
            title = "Progres",
            icon = Icons.Outlined.Schedule,
            route = "progress"
        )
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()), // Allow horizontal scrolling
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between items
    ) {
        navigationItems.forEach { item ->
            NavigationMenuItem(
                item = item,
                onClick = { navController.navigate(item.route) } // Navigate to the item's route
            )
        }
    }
}

@Composable
private fun NavigationMenuItem(
    item: NavigationItem,
    onClick: () -> Unit, // Pass an onClick lambda
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .shadow(12.dp, RoundedCornerShape(2.dp))
            .background(Color.White)
            .padding(8.dp)
            .clickable { onClick() }, // Call the onClick when this item is clicked
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        }
    }
}