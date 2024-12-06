
package com.dev.simeta.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dev.simeta.R

sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val title: String
) {
    object Home : BottomNavItem(
        route = "home",
        icon = R.drawable.icon_home,
        title = "Home"
    )
    object Logbook : BottomNavItem(
        route = "logbook",
        icon = R.drawable.icon_logbook,
        title = "Logbook"
    )
    object Bimbingan : BottomNavItem(
        route = "bimbingan",
        icon = R.drawable.icon_bimbingan,
        title = "Bimbingan"
    )
    object Profile : BottomNavItem(
        route = "profile",
        icon = R.drawable.icon_profile,
        title = "Profile"
    )
}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Logbook,
        BottomNavItem.Bimbingan,
        BottomNavItem.Profile
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFF00A67C)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color(0xFF00A67C),
                unselectedContentColor = Color.Gray
            )
        }
    }
}
