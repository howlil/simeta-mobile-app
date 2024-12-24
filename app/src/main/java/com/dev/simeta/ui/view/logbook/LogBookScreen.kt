package com.dev.simeta.ui.view.logbook

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.ui.components.DynamicTopBar
import com.dev.simeta.ui.view.logbook.logbook_components.LogbookCard

@Composable
fun LogBookScreen(
    navController: NavController,
    viewModel: LogbookViewModel = hiltViewModel()
) {
    val logbookState by viewModel.logbookState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchLogbooks(context)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DynamicTopBar(
            title = "LogBook",
            showAddButton = true,
            onAddClick = {
                navController.navigate("tambah_logbook")
            }
        )

        when (logbookState) {
            is LogbookViewModel.LogbookState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is LogbookViewModel.LogbookState.Success -> {
                val logbookItems = (logbookState as LogbookViewModel.LogbookState.Success).data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
                    items(logbookItems) { item ->
                        LogbookCard(
                            date = item.date,
                            description = item.activity,
                            status = "Terverifikasi", // Adjust based on your logic
                            onClick = {
                                navController.navigate("detail_logbook/${item.id}")
                            }
                        )
                    }
                }
            }
            is LogbookViewModel.LogbookState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (logbookState as LogbookViewModel.LogbookState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            LogbookViewModel.LogbookState.Idle -> Unit
        }
    }
}
