package com.dev.simeta.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTopBar(
    title: String,
    showBackButton: Boolean = false,
    showAddButton: Boolean = false,
    showEditDeleteButtons: Boolean = false,
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val showDeleteDialog = remember { mutableStateOf(false) }

    Box {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
            ),
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            },
            actions = {
                if (showAddButton) {
                    IconButton(onClick = onAddClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }
                if (showEditDeleteButtons) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF4CAF50)
                        )
                    }
                    IconButton(onClick = { showDeleteDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFFF3B30)
                        )
                    }
                }
            }
        )

        if (showDeleteDialog.value) {
            DeleteConfirmationDialog(
                onConfirm = {
                    onDeleteClick()
                    showDeleteDialog.value = false
                },
                onDismiss = { showDeleteDialog.value = false }
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(24.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "Hapus Logbook",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Text(
                text = "Apakah Anda yakin ingin menghapus logbook ini? Tindakan ini tidak dapat dibatalkan.",
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color(0xFF636366)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF3B30),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Hapus",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF636366)
                ),
                border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Batal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}