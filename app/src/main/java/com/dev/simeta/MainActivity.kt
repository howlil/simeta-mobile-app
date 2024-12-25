package com.dev.simeta

import ChangePasswordScreen
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.dev.simeta.ui.view.sidang.SidangScreen
import com.dev.simeta.ui.view.sidang.TambahSidangScreen
import com.dev.simeta.ui.view.user.UserScreen
import com.dev.simeta.ui.view.user.user_components.EditProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()

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

    // Meminta izin untuk POST_NOTIFICATIONS (Android 13+)
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    // Memproses hasil permintaan izin
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted.")
            } else {
                Log.e(TAG, "Notification permission denied.")
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
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
                UserScreen(navController = navController)
            }
            composable("ubah_password") {
                ChangePasswordScreen(navController = navController)
            }
            composable("ubah_user") {
                EditProfileScreen(navController = navController)
            }
            composable("tambah_logbook") {
                TambahLogbook(navController)
            }
            composable("sidang") {
                val context = LocalContext.current

                SidangScreen( context = context,navController = navController)
            }

            composable("add_sidang") {
                TambahSidangScreen( navController = navController)
            }
            composable("progress") {
                val context = LocalContext.current
                RiwayatProgressTA(context = context, navController = navController)            }
            composable("form_progress") {
                val context = LocalContext.current

                FormProgressTA(context = context, navController = navController )
            }
            composable("reminder") {
                val context = LocalContext.current

                ReminderScreen(context = context, navController = navController )
            }
            composable("tambah_reminder") {
                val context = LocalContext.current

                ReminderFormScreen(context = context, navController = navController )
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
