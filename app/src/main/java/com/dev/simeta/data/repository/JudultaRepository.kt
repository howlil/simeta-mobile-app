package com.dev.simeta.data.repository

import android.content.Context
import android.net.Uri
import com.dev.simeta.ui.view.judulta.JudultaItem
import javax.inject.Inject

class JudultaRepository @Inject constructor() {

    private val judultaItems = mutableListOf(
        JudultaItem(id = "1", title = "Implementasi Business Intelligence dalam Pengelolaan Sumber Daya Pariwisata di Bukittinggi", description = "Penelitian ini mengeksplorasi penerapan sistem informasi business intelligence dalam mengelola sumber daya pariwisata di Bukittinggi. Dengan menganalisis data pengunjung, umpan balik pelanggan, dan performa sektor pariwisata, sistem ini diharapkan dapat memberikan rekomendasi yang bermanfaat untuk meningkatkan pengalaman wisatawan dan efisiensi operasional"),
        JudultaItem(id = "2", title = "Sistem Pendukung Keputusan untuk Pemilihan Produk Kacamata di Toko Sidipingao dengan Metode TOPSIS", description = "Penelitian ini bertujuan untuk mengembangkan sistem pendukung keputusan (SPK) yang membantu pemilik toko kacamata Sidipingao dalam memilih produk kacamata yang akan dijual. Dengan mempertimbangkan berbagai faktor seperti tren pasar, preferensi pelanggan, dan margin keuntungan, sistem ini diharapkan dapat meningkatkan penjualan dan kepuasan pelanggan."),
        JudultaItem(id = "3", title = "Penerapan Enterprise Architecture untuk Meningkatkan Efisiensi dan Layanan Publik di Diskominfo Kota Padang", description = "Penelitian ini bertujuan untuk menerapkan Enterprise Architecture (EA) di Dinas Komunikasi dan Informatika Kota Padang untuk meningkatkan efisiensi operasional dan kualitas layanan publik. Dengan pendekatan yang sistematis, EA diharapkan dapat membantu Diskominfo dalam merencanakan dan mengelola infrastruktur TI, proses bisnis, serta alur informasi secara lebih efektif, sehingga dapat memberikan layanan yang lebih baik kepada masyarakat.")
    )

    fun createJudulta(
        context: Context,
        title: String,
        description: String,
        bidangPeminatan: String,
        dosenPembimbing: String,
        buktiMatkulTA: Uri?,
        buktiLulusKP: Uri?
    ): Boolean {
        val newId = (judultaItems.size + 1).toString()
        val newJudulta = JudultaItem(id = newId, title = title, description = description)
        judultaItems.add(newJudulta)
        return true
    }

    fun getJudultaItems(context: Context): List<JudultaItem> {
        return judultaItems
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
        val items = getJudultaItems(context)
        return items.find { it.id == judultaId } ?: JudultaItem(id = judultaId, title = "Judul TA $judultaId", description = "Deskripsi TA $judultaId")
    }

    fun updateJudulta(context: Context, id: String, title: String, description: String): Boolean {
        val index = judultaItems.indexOfFirst { it.id == id }
        return if (index != -1) {
            val updatedJudulta = judultaItems[index].copy(title = title, description = description)
            judultaItems[index] = updatedJudulta
            true
        } else {
            false
        }
    }

    fun deleteJudulta(context: Context, judultaId: String): Boolean {
        return judultaItems.removeIf { it.id == judultaId }
    }
}