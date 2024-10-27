package com.dev.simeta

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val outerPadding = if (isTablet()) 24.dp else 32.dp // Mengubah margin luar

    Box(modifier = Modifier.fillMaxSize()) {
        // Gambar latar belakang
        Image(
            painter = painterResource(id = R.drawable.background), // Gambar latar belakang
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop, // Memastikan gambar mengisi area yang tersedia
            modifier = Modifier.fillMaxSize() // Mengatur gambar untuk memenuhi layar
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(outerPadding), // Margin luar
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start // Mengatur alignment ke kiri
        ) {
            // Selamat Datang di SIMETA
            Text(
                text = "Selamat Datang di SIMETA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 1.dp) // Padding bawah
            )

            // Logo SIMETA sebagai Aset Vektor
            Image(
                painter = painterResource(id = R.drawable.logosimeta),
                contentDescription = "Logo SIMETA",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Jarak antar komponen

            // Login Akun
            Text(
                text = "Login Akun",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold, // Menjadikan teks bold
                modifier = Modifier.padding(bottom = 16.dp) // Padding bawah
            )

            // Label Email
            Text(
                text = "Email",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp) // Padding bawah untuk label
            )

            // Email Input menggunakan BasicTextField
            Box(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() // Validasi email
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, if (emailError) Color.Red else Color(0xFFB9B9B9), RoundedCornerShape(8.dp)) // Corner radius 8 dp
                        .padding(8.dp),
                    singleLine = true,
                    visualTransformation = VisualTransformation.None // Tidak ada transformasi visual untuk email
                )
                // Menambahkan placeholder untuk email
                if (email.isEmpty()) {
                    Text(
                        text = "@student.unand.ac.id",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp) // Align ke kiri
                    )
                }
            }
            if (emailError) {
                Text("Email tidak valid", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(8.dp)) // Jarak antar komponen

            // Label Password
            Text(
                text = "Password",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp) // Padding bawah untuk label
            )

            // Password Input menggunakan BasicTextField
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color(0xFFB9B9B9), RoundedCornerShape(8.dp)), // Corner radius 8 dp
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Menyembunyikan karakter password
                    singleLine = true
                )
                // Menambahkan placeholder untuk password
                if (password.isEmpty()) {
                    Text(
                        text = "**********",
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 0.dp) // Align ke kiri
                    )
                }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Jarak antar komponen

            // Tombol Login dengan warna hijau dan corner radius 8
            Button(
                onClick = {
                    // Logika login bisa ditambahkan di sini
                    onLoginSuccess()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009A4B)),
                modifier = Modifier
                    .fillMaxWidth() // Membuat tombol sepanjang form
                    .padding(vertical = 8.dp), // Padding dalam tombol
                shape = RoundedCornerShape(8.dp) // Corner radius 8 dp
            ) {
                Text("Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp)) // Jarak antar komponen
        }
    }
}

// Fungsi untuk mendeteksi apakah perangkat adalah tablet
fun isTablet(): Boolean {
    // Implementasi logika untuk mendeteksi tablet
    // Misalnya, berdasarkan ukuran layar atau densitas
    return false // Ganti dengan logika yang sesuai
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onLoginSuccess = {})
}