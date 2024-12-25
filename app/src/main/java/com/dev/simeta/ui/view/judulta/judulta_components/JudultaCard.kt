package com.dev.simeta.ui.view.judulta.judulta_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class VerificationStatus(
    val text: String,
    val backgroundColor: Color
)

@Composable
fun JudultaCard(
    title: String,
    description: String,
    status: String,
    onClick: () -> Unit
) {
    val verificationStatus = when (status) {
        "Disetujui" -> VerificationStatus(
            "Terverifikasi",
            Color(0xFF4CAF50)
        )
        "Menunggu" -> VerificationStatus(
            "Menunggu Disetujui",
            Color(0xFFFFEB3B)
        )
        else -> VerificationStatus(
            "Ditolak",
            Color(0xFFFF5252)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 8.dp)
            )
            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .background(
                        color = verificationStatus.backgroundColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    text = verificationStatus.text,
                    color = verificationStatus.backgroundColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}