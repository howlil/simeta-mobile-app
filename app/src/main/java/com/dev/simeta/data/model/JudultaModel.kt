package com.dev.simeta.data.model

data class JudultaItem(
    val id: String,
    val title: String,
    val description: String,
    val bidangPeminatan: String,
    val dosenPembimbing: String,
    val bukti1: String?,
    val bukti2: String?,
    val attachmentUrl: String? = null // Add this property
)