package com.dev.simeta.ui.view.logbook.logbook_pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.dev.simeta.data.model.DetailLogBook
import com.dev.simeta.ui.components.DynamicTopBar


@Composable
fun DetailLogbook(
    navController: NavController,
    logbookId: String,
    viewModel: DetailLogbookViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val detailLogbookState by viewModel.detailLogbookState.collectAsState()

    LaunchedEffect(logbookId) {
        viewModel.fetchLogbookDetail(context, logbookId)
    }

    when (detailLogbookState) {
        is DetailLogbookViewModel.DetailLogbookState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DetailLogbookViewModel.DetailLogbookState.Success -> {
            val logbook = (detailLogbookState as DetailLogbookViewModel.DetailLogbookState.Success).logbook
            DetailLogbookContent(
                logbook = logbook,
                navController = navController,
                viewModel = viewModel,
                context = context
            )
        }
        is DetailLogbookViewModel.DetailLogbookState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (detailLogbookState as DetailLogbookViewModel.DetailLogbookState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        DetailLogbookViewModel.DetailLogbookState.Idle -> Unit
    }
}

@Composable
fun DetailLogbookContent(
    logbook: DetailLogBook,
    navController: NavController,
    viewModel: DetailLogbookViewModel,
    context: Context
) {
    val scrollState = rememberScrollState()
    val deleteState by viewModel.deleteState.collectAsState()
    LaunchedEffect(deleteState) {
        when (deleteState) {
            is DetailLogbookViewModel.DeleteState.Success -> {
                Toast.makeText(
                    context,
                    (deleteState as DetailLogbookViewModel.DeleteState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
            is DetailLogbookViewModel.DeleteState.Error -> {
                Toast.makeText(
                    context,
                    (deleteState as DetailLogbookViewModel.DeleteState.Error).message,
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
            title = "Detail Logbook",
            showBackButton = true,
            showEditDeleteButtons = true,
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate("edit-logbook/${logbook.id}") },
            onDeleteClick = {
                viewModel.deleteLogbook(context, logbook.id)
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
                        text = logbook.date,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Column {
                        Text(
                            text = "Aktifitas",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = logbook.activity,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 20.sp
                        )
                    }

                    Column {
                        Text(
                            text = "Catatan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = logbook.notes,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            lineHeight = 20.sp
                        )
                    }

                    logbook.attachment_url?.let {
                        Column {
                            Text(
                                text = "Lampiran",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            val uriHandler = LocalUriHandler.current
                            Row(
                                modifier = Modifier
                                    .clickable { uriHandler.openUri(it) }
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
                                    text = it.substring(it.lastIndexOf("/") + 1),  // Show only filename
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