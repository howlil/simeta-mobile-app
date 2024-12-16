package com.dev.simeta.ui.view.logbook.logbook_pages

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.dev.simeta.ui.components.DynamicTopBar

@Composable
fun DetailLogbook (
    navController: NavController,
    logbookId: String
){
    DynamicTopBar(
        title = "Detail Logbook",
        showBackButton = true,
        onBackClick = {
            navController.popBackStack()
        }
    )
}