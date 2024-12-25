
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.ui.components.CustomPasswordTextField
import com.dev.simeta.ui.components.DynamicTopBar
import com.dev.simeta.ui.view.home.home_components.UserViewModel

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userState by viewModel.userState.collectAsState()
    val showToast by viewModel.showToast.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    // Show Toast for feedback
    if (showToast) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.dismissToast()
    }

    Scaffold(
        topBar = {
            DynamicTopBar(
                title = "Change Password",
                showAddButton = false,
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding) // Apply padding from Scaffold
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                CustomPasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "New Password",
                    placeholder = "********"
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomPasswordTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    placeholder = "********"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.changePassword(context, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009A4B))
                ) {
                    if (userState is UserViewModel.UserState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Change Password",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    )
}
