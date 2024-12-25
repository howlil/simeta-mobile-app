package com.dev.simeta.ui.view.user

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.dev.simeta.R
import com.dev.simeta.helpers.AuthPreferences
import com.dev.simeta.ui.view.home.home_components.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userState by viewModel.userState.collectAsState()

    // Load user data on screen load
    LaunchedEffect(Unit) {
        viewModel.fetchUserData(context)
    }

    when (userState) {
        is UserViewModel.UserState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Loading user data...")
            }
        }

        is UserViewModel.UserState.Success -> {
            val userData = (userState as UserViewModel.UserState.Success).data
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    painter = painterResource(id = R.drawable.howl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = userData.full_name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = userData.email,
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                NavMenuItem(text = "Edit Profile", onClick = { navController.navigate("ubah_user") })
                NavMenuItem(
                    text = "Ubah Password",
                    onClick = { navController.navigate("ubah_password") }
                )
                NavMenuItem(text = "Pengaturan", onClick = { /* Navigate to Settings */ })
                NavMenuItem(
                    text = "Log out",
                    onClick = {
                            navController.navigate("login")
                    },
                    color = Color.Red
                )
            }
        }

        is UserViewModel.UserState.Error -> {
            val errorMessage = (userState as UserViewModel.UserState.Error).message
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Error: $errorMessage", color = Color.Red)
            }
        }

        UserViewModel.UserState.Idle -> Unit
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
            .background(Color.Transparent)
            .padding(horizontal = 16.dp)
    )
}

// Updated logout function
fun UserViewModel.logout(context: Context, onLogoutSuccess: () -> Unit) {
    viewModelScope.launch {
        try {
            AuthPreferences.clearAuthToken(context)
            onLogoutSuccess()
        } catch (e: Exception) {
            Log.e("UserViewModel", "Logout failed: ${e.message}")
        }
    }
}
