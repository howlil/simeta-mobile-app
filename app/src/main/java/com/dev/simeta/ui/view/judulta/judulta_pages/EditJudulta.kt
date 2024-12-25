package com.dev.simeta.ui.view.judulta.judulta_pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.ui.components.DynamicTopBar
import com.dev.simeta.ui.view.judulta.JudultaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditJudulta(
    navController: NavController,
    judultaId: String,
    viewModel: EditJudultaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val detailJudultaState by viewModel.detailJudultaState.collectAsState()

    LaunchedEffect(judultaId) {
        viewModel.fetchJudultaDetail(context, judultaId)
    }

    when (detailJudultaState) {
        is EditJudultaViewModel.DetailJudultaState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is EditJudultaViewModel.DetailJudultaState.Success -> {
            val judulta = (detailJudultaState as EditJudultaViewModel.DetailJudultaState.Success).judulta
            EditJudultaContent(
                judulta = judulta,
                navController = navController,
                viewModel = viewModel,
                context = context
            )
        }
        is EditJudultaViewModel.DetailJudultaState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (detailJudultaState as EditJudultaViewModel.DetailJudultaState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        EditJudultaViewModel.DetailJudultaState.Idle -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditJudultaContent(
    judulta: JudultaItem,
    navController: NavController,
    viewModel: EditJudultaViewModel,
    context: Context
) {
    var title by remember { mutableStateOf(judulta.title) }
    var description by remember { mutableStateOf(judulta.description) }
    val scrollState = rememberScrollState()
    val updateState by viewModel.updateState.collectAsState()

    LaunchedEffect(updateState) {
        when (updateState) {
            is EditJudultaViewModel.UpdateState.Success -> {
                Toast.makeText(
                    context,
                    (updateState as EditJudultaViewModel.UpdateState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
            is EditJudultaViewModel.UpdateState.Error -> {
                Toast.makeText(
                    context,
                    (updateState as EditJudultaViewModel.UpdateState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        DynamicTopBar(
            title = "Edit Judul TA",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
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
                    // Judul Field
                    Column {
                        Text(
                            text = "Judul TA",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color(0xFFF5F5F5),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            placeholder = {
                                Text(
                                    "Masukkan judul TA",
                                    color = Color.Gray
                                )
                            }
                        )
                    }

                    // Deskripsi Field
                    Column {
                        Text(
                            text = "Deskripsi TA",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color(0xFFF5F5F5),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            placeholder = {
                                Text(
                                    "Tambahkan deskripsi jika diperlukan",
                                    color = Color.Gray
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Submit
            Button(
                onClick = {
                    viewModel.updateJudulta(
                        context = context,
                        id = judulta.id,
                        title = title,
                        description = description
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
                enabled = title.isNotBlank() && description.isNotBlank()
            ) {
                if (updateState is EditJudultaViewModel.UpdateState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Simpan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}