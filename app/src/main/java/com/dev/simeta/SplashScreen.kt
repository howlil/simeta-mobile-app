// SplashScreen.kt
package com.dev.simeta

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Gambar latar belakang terikat ke atas layar
        Image(
            painter = painterResource(id = R.drawable.bgunand), // Gambar latar belakang
            contentDescription = "Background Image",
            modifier = Modifier
                 // Mengatur lebar gambar latar belakang
                .align(Alignment.TopCenter) // Mengatur posisi gambar ke atas
        )

        // Gambar logo Simeta di tengah layar
        Image(
            painter = painterResource(id = R.drawable.logosimeta),
            contentDescription = "Logo Simeta",
            modifier = Modifier.size(384.dp) // Mengatur ukuran logo menjadi 384 dp
        )
    }

    // Delay selama 2 detik sebelum pindah ke MainScreen
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    val navController = rememberNavController()
    SplashScreen(navController = navController)
}