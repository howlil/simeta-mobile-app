package com.dev.simeta

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showPasswordGuidelines by remember { mutableStateOf(false) }

    val outerPadding = if (isTablet()) 24.dp else 32.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Gambar latar belakang
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Selamat Datang di SIMETA
            Text(
                text = "Selamat Datang di SIMETA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 1.dp)
            )

            // Logo SIMETA
            Image(
                painter = painterResource(id = R.drawable.logosimeta),
                contentDescription = "Logo SIMETA",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(1.dp))

            // Login Akun
            Text(
                text = "Login Akun",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Label Email
            Text(
                text = "Email",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Email Input
            Box(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, if (emailError) Color.Red else Color(0xFFB9B9B9), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    singleLine = true,
                    visualTransformation = VisualTransformation.None
                )
                if (email.isEmpty()) {
                    Text(
                        text = "@student.unand.ac.id",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
                    )
                }
            }
            if (emailError) {
                Text("Email tidak valid", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Label Password
            Text(
                text = "Password",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Password Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color(0xFFB9B9B9), RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = !isPasswordValid(it) // Validasi password
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .onFocusChanged { focusState ->
                            showPasswordGuidelines = focusState.isFocused // Tampilkan pedoman saat fokus
                        },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true
                )
                if (password.isEmpty()) {
                    Text(
                        text = "**********",
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 0.dp)
                    )
                }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }

            if (passwordError) {
                Text("Password tidak valid", color = Color.Red, fontSize = 12.sp)
            }

            // Tampilkan pedoman password jika input password sedang fokus
            if (showPasswordGuidelines) {
                Text(
                    text = "Password harus memiliki minimal 8 karakter, termasuk huruf besar, huruf kecil, angka, dan simbol.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Login
            Button(
                onClick = {
                    if (!emailError && !passwordError && email.isNotEmpty() && password.isNotEmpty()) {
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, "Periksa email dan password Anda.", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009A4B)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Fungsi untuk validasi password
fun isPasswordValid(password: String): Boolean {
    return password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() } &&
            password.any { !it.isLetterOrDigit() }
}

// Fungsi untuk mendeteksi apakah perangkat adalah tablet
fun isTablet(): Boolean {
    return false // Ganti dengan logika yang sesuai
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onLoginSuccess = {})
}