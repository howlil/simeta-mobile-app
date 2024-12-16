package com.dev.simeta.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.simeta.ui.components.DynamicTopBar
import com.dev.simeta.ui.view.logbook.logbook_components.LogbookCard

data class LogbookItem(
    val id: String,
    val date: String,
    val month: String,
    val description: String,
    val status: String
)

@Composable
fun LogBookScreen(navController: NavController) {
    // Sample logbook entries
    val logbookItems = listOf(
        LogbookItem(
            id = "1",
            date = "Senin",
            month = "12 Oktober 2024",
            description = "Lorem ipsum dolor sit amet consectetur. Nascetur egestas mauris amet lectus sem mauris sodales molestie. Tristique",
            status = "Terverifikasi"
        ),
        LogbookItem(
            id = "2",
            date = "Senin",
            month = "12 Oktober 2024",
            description = "Lorem ipsum dolor sit amet consectetur. Nascetur egestas mauris amet lectus sem mauris sodales molestie. Tristique",
            status = "Belum Diverifikasi"
        ),
        // Add IDs to other items...
    )

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
                    month = item.month,
                    description = item.description,
                    status = item.status,
                    onClick = {
                        navController.navigate("detail_logbook/${item.id}")
                    }
                )
            }
        }
    }
}