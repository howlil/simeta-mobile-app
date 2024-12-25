package com.dev.simeta.ui.view.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dev.simeta.ui.components.CustomPasswordTextField
import com.dev.simeta.ui.components.CustomToast
import com.dev.simeta.ui.components.DynamicTopBar
import com.dev.simeta.ui.components.ToastType

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    var oldPassword by remember { mutableStateOf("@Test123") }
    var newPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    val showToast by viewModel.showToast.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DynamicTopBar(
            title = "Ubah Password",
            showAddButton = false,
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            onAddClick = { /* No action needed */ }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CustomPasswordTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = "Password Lama",
                placeholder = "********"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomPasswordTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "Password Baru",
                placeholder = "********"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.changePassword(oldPassword, newPassword, context)
                    oldPassword = newPassword // Update old password to new password
                    newPassword = "" // Clear new password field
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009A4B))
            ) {
                Text(
                    text = "Ubah Password",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (showToast) {
                CustomToast(
                    message = toastMessage,
                    isVisible = true,
                    type = ToastType.SUCCESS,
                    onDismiss = { viewModel.dismissToast() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    val navController = rememberNavController()
    ChangePasswordScreen(navController = navController)
}