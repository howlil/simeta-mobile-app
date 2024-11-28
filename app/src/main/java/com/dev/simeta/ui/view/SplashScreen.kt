package com.dev.simeta.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.dev.simeta.R
import com.dev.simeta.helpers.AuthPreferences

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash), // Splash image
            contentDescription = "Splash Image",
            contentScale = ContentScale.Crop, // Ensure the image fills the available area
            modifier = Modifier.fillMaxSize() // Set the image to fill the screen
        )

        // Navigate away after 2 seconds
        LaunchedEffect(Unit) {
            delay(2000)
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
}
