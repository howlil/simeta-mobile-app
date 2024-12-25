package com.dev.simeta.data.repository

import android.content.Context
import android.net.Uri
import com.dev.simeta.ui.view.judulta.JudultaItem
import javax.inject.Inject

class JudultaRepository @Inject constructor() {

    fun createJudulta(
        context: Context,
        title: String,
        description: String,
        bidangPeminatan: String,
        dosenPembimbing: String,
        buktiMatkulTA: Uri?,
        buktiLulusKP: Uri?
    ): Boolean {
        // Implement your logic to create Judul TA here
        // For now, let's simulate a successful creation
        return true
    }

    fun getJudultaItems(context: Context): List<JudultaItem> {
        // Implement your data fetching logic here
        // For now, let's return a dummy list of JudultaItem
        return listOf(
            JudultaItem(id = "1", title = "Judul TA 1", description = "Deskripsi TA 1"),
            JudultaItem(id = "2", title = "Judul TA 2", description = "Deskripsi TA 2"),
            JudultaItem(id = "3", title = "Judul TA 3", description = "Deskripsi TA 3")
        )
    }

    fun getBidangPeminatan(): List<String> {
        return listOf(
            "Bussiness Intelligence",
            "Enterprise Application",
            "Sistem Penunjang Keputusan",
            "Geographic Information System"
        )
    }

    fun getDosenPembimbing(): List<String> {
        return listOf(
            "Prof. Ir. Surya Afnarius, PhD",
            "Husnil Kamil, M.T",
            "Hasdi Putra, M.T",
            "Ricky Akbar, M. Kom",
            "Fajril Akbar, M.Sc",
            "Haris Suryamen, M.Sc",
            "Jefril Rahmadoni, M.Kom",
            "Adi Arga Afrinur, M.Kom",
            "Afriyanti Dwi Kartika, M.T",
            "Dwi Welly Sukma Nirad, M.T",
            "Hafizah Hanim, M.Kom",
            "Ullya Mega Wahyuni, M.Kom",
            "Rahmatika Pratama Santi, M.T",
            "Febby Apri Wenando, M.Eng",
            "Aina Hubby Aziira, M.Eng"
        )
    }

    fun getJudultaDetail(context: Context, judultaId: String): JudultaItem {
        // Implement your logic to fetch Judul TA detail here
        // For now, let's return a dummy JudultaItem
        return JudultaItem(id = judultaId, title = "Judul TA $judultaId", description = "Deskripsi TA $judultaId")
    }

    fun deleteJudulta(context: Context, judultaId: String): Boolean {
        // Implement your logic to delete Judul TA here
        // For now, let's simulate a successful deletion
        return true
    }
}