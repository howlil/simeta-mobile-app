package com.dev.simeta.ui.view.judulta

data class JudultaItem(
    val id: String,
    val title: String,
    val description: String,
    val attachmentUrl: String? = null // Add this property
)