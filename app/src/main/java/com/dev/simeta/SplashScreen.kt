package com.dev.simeta

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.compose.rememberNavController

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(), // Mengatur kotak untuk memenuhi ukuran layar
        contentAlignment = Alignment.Center
    ) {
        // Gambar splash yang memenuhi layar
        Image(
            painter = painterResource(id = R.drawable.splash), // Gambar splash
            contentDescription = "Splash Image",
            contentScale = ContentScale.Crop, // Memastikan gambar mengisi area yang tersedia
            modifier = Modifier.fillMaxSize() // Mengatur gambar untuk memenuhi layar
        )
    }

    // Delay selama 2 detik sebelum pindah ke LoginScreen
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("login") { // Pindah ke LoginScreen
            popUpTo("splash") { inclusive = true } // Menghapus splash dari stack
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}