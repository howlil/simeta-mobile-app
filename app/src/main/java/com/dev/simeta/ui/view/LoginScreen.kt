@file:OptIn(ExperimentalMaterial3Api::class)

package com.dev.simeta.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.simeta.R
import com.dev.simeta.ui.components.CustomPasswordTextField
import com.dev.simeta.ui.components.CustomTextField
import com.dev.simeta.ui.components.CustomToast
import com.dev.simeta.ui.components.ToastType
import com.dev.simeta.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsState()
    var currentMessageIndex by remember { mutableStateOf(0) }
    var messagesToDisplay by remember { mutableStateOf<List<String>>(emptyList()) }
    var showToast by remember { mutableStateOf(false) }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginViewModel.LoginState.Success -> {
                toastType = ToastType.SUCCESS
                messagesToDisplay = listOf("Login Successful!")
                showToast = true
                onLoginSuccess()
            }
            is LoginViewModel.LoginState.Error -> {
                toastType = ToastType.ERROR
                messagesToDisplay = (loginState as LoginViewModel.LoginState.Error).messages
                showToast = true
            }
            else -> {}
        }
    }
    // Display the toast one by one
    LaunchedEffect(showToast) {
        if (showToast && messagesToDisplay.isNotEmpty()) {
            for (message in messagesToDisplay) {
                currentMessageIndex = messagesToDisplay.indexOf(message)
                kotlinx.coroutines.delay(1500) // Delay 3 seconds between messages
            }
            showToast = false // Reset toast visibility after all messages
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.9f),
                            Color.White
                        ),
                        startY = 300f
                    )
                )
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Welcome Text
            Spacer(modifier = Modifier.height(48.dp))


            Image(
                painter = painterResource(id = R.drawable.logosimeta),
                contentDescription = "Logo SIMETA",
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.Start)
            )

            Text(
                text = "Selamat Datang\nDi SIMETA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "Sistem Informasi Mengelola Tugas Akhir",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))


            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "@student.unand.ac.id"
            )


            Spacer(modifier = Modifier.height(8.dp))

            // Password Input
            CustomPasswordTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "********"
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009A4B))
            ) {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

        }

        if (showToast && currentMessageIndex < messagesToDisplay.size) {
            CustomToast(
                message = messagesToDisplay[currentMessageIndex],
                isVisible = true,
                type = toastType,
                onDismiss = { /* No action needed */ }
            )
        }
    }
}

