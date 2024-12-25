package com.dev.simeta.ui.view.sidang

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.ui.components.DynamicTopBar

@Composable
fun SidangScreen(
    navController: NavController,
    context: Context,
    viewModel: SidangViewModel = hiltViewModel()
) {
    val sidangState by viewModel.sidangState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchSidang(context )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DynamicTopBar(
            title = "Sidang",
            showAddButton = true,
            showBackButton = true,
            onBackClick = { navController.popBackStack() },
            onAddClick = {
                navController.navigate("add_sidang")
            }
        )

        when (sidangState) {
            is SidangViewModel.SidangState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SidangViewModel.SidangState.Success -> {
                val sidangItems = (sidangState as SidangViewModel.SidangState.Success).data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(sidangItems) { item ->
                        SidangCard(
                            date = item.tanggal,
                            time = item.jam,
                            status = item.status,
                            documentUrl = item.dokumen,
                            onClick = {
                                // Handle click action
                            }
                        )
                    }
                }
            }
            is SidangViewModel.SidangState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (sidangState as SidangViewModel.SidangState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            SidangViewModel.SidangState.Idle -> Unit
        }
    }
}

@Composable
fun SidangCard(
    date: String,
    time: String,
    status: String,
    documentUrl: String,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor = when (status.uppercase()) {
        "PENDING" -> MaterialTheme.colorScheme.secondary
        "APPROVED" -> MaterialTheme.colorScheme.primary
        "REJECTED" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .clickable { onClick?.invoke() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Date and Time
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Status and Document
            Column(
                modifier = Modifier.weight(0.6f),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = status,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = Color.White
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Document: $documentUrl",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
