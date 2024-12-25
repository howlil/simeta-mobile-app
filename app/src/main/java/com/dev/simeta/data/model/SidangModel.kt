package com.dev.simeta.data.model

data class Sidang(
    val id: String,
    val tanggal: String,
    val jam: String,
    val dokumen: String,
    val status: String,
    val created_at: String,
    val updated_at: String
)

data class SidangResponse(
    val error: Boolean,
    val messages: String,
    val data: List<Sidang>
)

data class SidangResponseCreate(
    val error: Boolean,
    val messages: String,
    val data: SidangData
)

data class SidangData(
    val id: String,
    val tanggal: String,
    val jam: String,
    val dokumen: String,
    val status: String,
    val ta_id: String,
    val created_at: String,
    val updated_at: String
)
