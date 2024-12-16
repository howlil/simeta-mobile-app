package com.dev.simeta.ui.view.logbook.logbook_pages


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.navigation.NavController
import com.dev.simeta.ui.components.DynamicTopBar


@Composable
fun TambahLogbook(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        DynamicTopBar(
            title = "Tambah Logbook",
            showBackButton = true,
            onBackClick = {
                navController.popBackStack()
            }
        )

    }


}