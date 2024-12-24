package com.dev.simeta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dev.simeta.ui.components.BottomBar
import com.dev.simeta.ui.components.BottomNavItem
import com.dev.simeta.ui.theme.SimetaTheme
import com.dev.simeta.ui.view.*
import com.dev.simeta.ui.view.bimbingan.BimbinganScreen
import com.dev.simeta.ui.view.home.HomeScreen
import com.dev.simeta.ui.view.logbook.LogBookScreen
import com.dev.simeta.ui.view.logbook.logbook_pages.DetailLogbook
import com.dev.simeta.ui.view.logbook.logbook_pages.TambahLogbook
import com.dev.simeta.ui.view.login.LoginScreen
import com.dev.simeta.ui.view.progress_ta.FormProgressTA
import com.dev.simeta.ui.view.progress_ta.RiwayatProgressTA
import com.dev.simeta.ui.view.reminder.ReminderFormScreen
import com.dev.simeta.ui.view.reminder.ReminderScreen
import com.dev.simeta.ui.view.user.UserScreen
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            SimetaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen { navController.navigate("main") }
        }
        composable("main") {
            MainScreenWithBottomBar(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomBar(mainNavController: NavController) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController)
            }
            composable(BottomNavItem.Logbook.route) {
                LogBookScreen(navController)
            }
            composable(BottomNavItem.Bimbingan.route) {
                BimbinganScreen()
            }
            composable(BottomNavItem.Profile.route) {
                UserScreen()
            }
            composable("tambah_logbook") {
                TambahLogbook(navController)
            }
            composable("progress") {
                val context = LocalContext.current
                RiwayatProgressTA(context = context, navController = navController)
            }
            composable("form_progress") {
                val context = LocalContext.current
                FormProgressTA(context = context, navController = navController)
            }
            composable("reminder") {
                val context = LocalContext.current
                ReminderScreen(context = context, navController = navController)
            }
            composable("tambah_reminder") {
                val context = LocalContext.current
                ReminderFormScreen(context = context, navController = navController)
            }
            composable(
                route = "detail_logbook/{logbookId}",
                arguments = listOf(
                    navArgument("logbookId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                DetailLogbook(
                    navController = navController,
                    logbookId = backStackEntry.arguments?.getString("logbookId") ?: ""
                )
            }
        }
    }
}
