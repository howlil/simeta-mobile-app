package com.dev.simeta.ui.view.judulta

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dev.simeta.ui.components.DynamicTopBar
import com.dev.simeta.ui.view.judulta.judulta_components.JudultaCard

@Composable
fun JudultaScreen(
    navController: NavController,
    viewModel: JudultaViewModel = hiltViewModel()
) {
    val judultaState by viewModel.judultaState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchJudulta(context)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DynamicTopBar(
            title = "Judul TA",
            showAddButton = true,
            onAddClick = {
                navController.navigate("tambah_judulta")
            }
        )

        when (judultaState) {
            is JudultaViewModel.JudultaState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is JudultaViewModel.JudultaState.Success -> {
                val judultaItems = (judultaState as JudultaViewModel.JudultaState.Success).data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                ) {
                    items(judultaItems) { item ->
                        JudultaCard(
                            title = item.title,
                            description = item.description,
                            status = "Menunggu", // Adjust based on your logic
                            onClick = {
                                navController.navigate("detail_judulta/${item.id}")
                            }
                        )
                    }
                }
            }
            is JudultaViewModel.JudultaState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (judultaState as JudultaViewModel.JudultaState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            JudultaViewModel.JudultaState.Idle -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JudultaScreenPreview() {
    val navController = rememberNavController()
    JudultaScreen(navController = navController)
}