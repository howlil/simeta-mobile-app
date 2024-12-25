package com.dev.simeta.ui.view.judulta.judulta_pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.ui.components.DynamicTopBar
import com.dev.simeta.ui.view.judulta.JudultaItem

@Composable
fun DetailJudulta(
    navController: NavController,
    judultaId: String,
    viewModel: DetailJudultaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val detailJudultaState by viewModel.detailJudultaState.collectAsState()

    LaunchedEffect(judultaId) {
        viewModel.fetchJudultaDetail(context, judultaId)
    }

    when (detailJudultaState) {
        is DetailJudultaViewModel.DetailJudultaState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DetailJudultaViewModel.DetailJudultaState.Success -> {
            val judulta = (detailJudultaState as DetailJudultaViewModel.DetailJudultaState.Success).judulta
            DetailJudultaContent(
                judulta = judulta,
                navController = navController,
                viewModel = viewModel,
                context = context
            )
        }
        is DetailJudultaViewModel.DetailJudultaState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (detailJudultaState as DetailJudultaViewModel.DetailJudultaState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        DetailJudultaViewModel.DetailJudultaState.Idle -> Unit
    }
}

@Composable
fun DetailJudultaContent(
    judulta: JudultaItem,
    navController: NavController,
    viewModel: DetailJudultaViewModel,
    context: Context
) {
    val scrollState = rememberScrollState()
    val deleteState by viewModel.deleteState.collectAsState()
    LaunchedEffect(deleteState) {
        when (deleteState) {
            is DetailJudultaViewModel.DeleteState.Success -> {
                Toast.makeText(
                    context,
                    (deleteState as DetailJudultaViewModel.DeleteState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
            is DetailJudultaViewModel.DeleteState.Error -> {
                Toast.makeText(
                    context,
                    (deleteState as DetailJudultaViewModel.DeleteState.Error).message,
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
            title = "Detail Judul TA",
            showBackButton = true,
            showEditDeleteButtons = true,
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate("edit-judulta/${judulta.id}") },
            onDeleteClick = {
                viewModel.deleteJudulta(context, judulta.id)
            }
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
                    Text(
                        text = "Terverifikasi",
                        color = Color(0xFF00A67C),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = judulta.title,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Column {
                        Text(
                            text = "Deskripsi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = judulta.description,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 20.sp
                        )
                    }

                    judulta.attachmentUrl?.let { attachmentUrl ->
                        Column {
                            Text(
                                text = "Lampiran",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            val uriHandler = LocalUriHandler.current
                            Row(
                                modifier = Modifier
                                    .clickable { uriHandler.openUri(attachmentUrl) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachFile,
                                    contentDescription = "Attachment",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = attachmentUrl.substring(attachmentUrl.lastIndexOf("/") + 1),  // Show only filename
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}