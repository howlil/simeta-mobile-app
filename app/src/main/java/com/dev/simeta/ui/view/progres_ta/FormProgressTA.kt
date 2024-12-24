package com.dev.simeta.ui.view.progress_ta

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.data.model.CreateProgressRequest
import com.dev.simeta.ui.components.DynamicTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormProgressTA(
    navController: NavController,
    context: Context,
    viewModel: FormProgressTAViewModel = hiltViewModel()
) {
    val milestoneState by viewModel.milestoneState.collectAsState()
    val createProgressState by viewModel.createProgressState.collectAsState()

    var selectedMilestoneId by remember { mutableStateOf<String?>(null) }
    var selectedMilestoneName by remember { mutableStateOf("Select Milestone") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchMilestones(context)
    }

    LaunchedEffect(createProgressState) {
        when (createProgressState) {
            is FormProgressTAViewModel.CreateProgressState.Success -> {
                Toast.makeText(
                    context,
                    (createProgressState as FormProgressTAViewModel.CreateProgressState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
            is FormProgressTAViewModel.CreateProgressState.Error -> {
                Toast.makeText(
                    context,
                    (createProgressState as FormProgressTAViewModel.CreateProgressState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    // Parent Box to stack the TopBar and Content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        DynamicTopBar(
            title = "Add Progress TA",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        // Scrollable Form Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 72.dp) // Adjust top padding for TopBar
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Form Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Milestone Dropdown
                    Column {
                        Text(
                            text = "Milestone",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .clickable { isDropdownExpanded = !isDropdownExpanded }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = selectedMilestoneName,
                                color = if (selectedMilestoneId == null) Color.Gray else Color.Black
                            )
                        }
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            modifier = Modifier
                                .widthIn(min = 150.dp, max = 300.dp) // Adjust dropdown width here
                                .background(Color.White)
                        ) {
                            when (milestoneState) {
                                is FormProgressTAViewModel.MilestoneState.Loading -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                                is FormProgressTAViewModel.MilestoneState.Success -> {
                                    val milestones =
                                        (milestoneState as FormProgressTAViewModel.MilestoneState.Success).milestones
                                    milestones.forEach { milestone ->
                                        DropdownMenuItem(
                                            text = { Text(milestone.name) },
                                            onClick = {
                                                selectedMilestoneId = milestone.id
                                                selectedMilestoneName = milestone.name
                                                isDropdownExpanded = false
                                            },
                                            modifier = Modifier.fillMaxWidth() // Ensure each item respects the dropdown's width
                                        )
                                    }
                                }
                                is FormProgressTAViewModel.MilestoneState.Error -> {
                                    Text(
                                        text = (milestoneState as FormProgressTAViewModel.MilestoneState.Error).message,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                                FormProgressTAViewModel.MilestoneState.Idle -> Unit
                            }
                        }
                    }

                    // Title Input
                    Column {
                        Text(
                            text = "Title",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("Enter progress title") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color(0xFFF5F5F5),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            )
                        )
                    }

                    // Details Input
                    Column {
                        Text(
                            text = "Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = details,
                            onValueChange = { details = it },
                            placeholder = { Text("Enter progress details") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .shadow(2.dp, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color(0xFFF5F5F5),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    viewModel.createProgress(
                        context = context,
                        request = CreateProgressRequest(
                            milestone_id = selectedMilestoneId ?: "",
                            title = title,
                            details = details
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00A67C),
                    disabledContainerColor = Color(0xFF00A67C).copy(alpha = 0.5f)
                ),
                enabled = title.isNotBlank() && details.isNotBlank()
            ) {
                if (createProgressState is FormProgressTAViewModel.CreateProgressState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Submit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
