package com.dev.simeta.ui.view.home_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MilestoneCard(
    title: String = "Kerjakan Milestone",
    date: String = "20 Oktober 2024",
    logbookCount: String = "10/16",
    milestone: String = "Seminar TA",
    status: String = "Pending",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Make container transparent to add custom gradient
        )
    ) {
        // Gradient background with top-to-bottom gradient direction
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF00E676),
                            Color(0xFF00A67C)
                        ) // Top to bottom gradient colors
                    )
                )
                .clip(RoundedCornerShape(24.dp)) // Round the corners
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.15f)) // Lighter rounded background
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = date,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Logbook Status
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 8.dp) // Align start and add horizontal padding
                    ) {
                        Text(
                            text = "Logbook",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.35f)) // Highlight the value background specifically
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = logbookCount,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Milestone Status
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 8.dp) // Align start and add horizontal padding
                    ) {
                        Text(
                            text = "Milestone",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.35f)) // Highlight the value background specifically
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = milestone,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Current Status
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(horizontal = 8.dp) // Align start and add horizontal padding
                    ) {
                        Text(
                            text = "Status",
                            color = Color.White,
                            fontSize = 14.sp,

                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.35f)) // Highlight the value background specifically
                                .padding(horizontal = 20.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = status,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
