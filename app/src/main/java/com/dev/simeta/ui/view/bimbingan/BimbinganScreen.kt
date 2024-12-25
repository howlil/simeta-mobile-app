@file:OptIn(ExperimentalMaterial3Api::class)

package com.dev.simeta.ui.view
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JadwalBimbinganScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Jadwal Bimbingan") }, actions = {
                IconButton(onClick = { /* Tambahkan aksi */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            },
            )},
        bottomBar = {
            BottomNavigationBar()
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.padding(16.dp)
        ) {
            items(6) { index ->
                JadwalBimbinganCard(
                    day = "Senin",
                    date = "12 Oktober 2024",
                    description = "Lorem ipsum dolor sit amet consectetur. Nascetur egestas mauris amet lectus sem mauris sodales molestie. Tristique",
                    status = when (index) {
                        0 -> "On Progress"
                        1 -> "Selesai"
                        2 -> "Dibatalkan"
                        else -> "Terverifikasi"
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun JadwalBimbinganCard(day: String, date: String, description: String, status: String) {
    val backgroundColor = when (status) {
        "On Progress" -> Color(0xFFFFF9C4) // Kuning Muda
        "Selesai" -> Color(0xFFC8E6C9) // Hijau Muda
        "Dibatalkan" -> Color(0xFFFFCDD2) // Merah Muda
        else -> Color(0xFFC8E6C9) // Hijau Muda (Terverifikasi)
    }

    val textColor = when (status) {
        "On Progress" -> Color(0xFFFFA000) // Kuning Gelap
        "Selesai" -> Color(0xFF388E3C) // Hijau Gelap
        "Dibatalkan" -> Color(0xFFD32F2F) // Merah Gelap
        else -> Color(0xFF388E3C) // Hijau Gelap
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .shadow(1.dp, RoundedCornerShape(8.dp)), shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = day)
            Text(text = date, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = status, color = textColor)
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    val items = listOf("Home", "Logbook", "Bimbingan", "Profile")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Book,
        Icons.Default.People,
        Icons.Default.Person
    )
    val selectedIndex = remember { mutableStateOf(2) } // Default: "Bimbingan"

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedIndex.value == index,
                onClick = { selectedIndex.value = index },
                selectedContentColor = Color(0xFF4CAF50),
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@Composable
fun BottomNavigationItem(icon: @Composable () -> Unit, label: @Composable () -> Unit, selected: Boolean, onClick: () -> Unit, selectedContentColor: Color, unselectedContentColor: Color) {
}

@Composable
fun BottomNavigation(backgroundColor: Color, contentColor: Color, content: @Composable () -> Unit) {
}

@Preview(showBackground = true)
@Composable
fun PreviewJadwalBimbinganScreen() {
    JadwalBimbinganScreen()
}