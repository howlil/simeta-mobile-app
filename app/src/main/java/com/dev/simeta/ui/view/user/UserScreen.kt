package com.dev.simeta.ui.view.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dev.simeta.R

@Composable
fun UserScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.profile_background),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mhd Ulil Abshar",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "2211521003_ulil@student.ac.id",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        NavMenuItem(text = "Edit Profile", onClick = { /* Navigate to Edit Profile */ })
        NavMenuItem(text = "Ubah Password", onClick = { navController.navigate("change_password_screen") })
        NavMenuItem(text = "Pengaturan", onClick = { /* Navigate to Settings */ })
        NavMenuItem(text = "Log out", onClick = { /* Handle Log out */ }, color = Color.Red)
    }
}

@Composable
fun NavMenuItem(text: String, onClick: () -> Unit, color: Color = Color.Black) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    val navController = rememberNavController()
    UserScreen(navController = navController)
}