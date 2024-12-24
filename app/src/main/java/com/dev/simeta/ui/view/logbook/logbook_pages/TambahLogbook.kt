package com.dev.simeta.ui.view.logbook.logbook_pages

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.simeta.ui.components.DynamicTopBar
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahLogbook(
    navController: NavController,
    viewModel: TambahLogbookViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var aktivitas by remember { mutableStateOf("") }
    var catatan by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val createState by viewModel.createState.collectAsState()

    val dateFormatter = remember { SimpleDateFormat("dd MMMM yyyy", Locale("id")) }
    val apiDateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale("id")) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFile = it
            selectedFileName = getFileName(context, it)
        }
    }

    LaunchedEffect(createState) {
        when (createState) {
            is TambahLogbookViewModel.CreateLogbookState.Success -> {
                Toast.makeText(
                    context,
                    (createState as TambahLogbookViewModel.CreateLogbookState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
            is TambahLogbookViewModel.CreateLogbookState.Error -> {
                Toast.makeText(
                    context,
                    (createState as TambahLogbookViewModel.CreateLogbookState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate.timeInMillis
                ).also { state ->
                    state.selectedDateMillis?.let { millis ->
                        selectedDate.timeInMillis = millis
                    }
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        DynamicTopBar(
            title = "Tambah Logbook",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Tanggal Field
                    Column {
                        Text(
                            text = "Tanggal",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                .clickable { showDatePicker = true }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                tint = Color(0xFF00A67C)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = dateFormatter.format(selectedDate.time),
                                color = Color.DarkGray
                            )
                        }
                    }

                    // Aktivitas Field
                    Column {
                        Text(
                            text = "Aktivitas",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = aktivitas,
                            onValueChange = { aktivitas = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color(0xFFF5F5F5),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            placeholder = {
                                Text(
                                    "Masukkan aktivitas anda hari ini",
                                    color = Color.Gray
                                )
                            }
                        )
                    }

                    // Catatan Field
                    Column {
                        Text(
                            text = "Catatan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = catatan,
                            onValueChange = { catatan = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = Color(0xFFF5F5F5),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(8.dp),
                            placeholder = {
                                Text(
                                    "Tambahkan catatan jika diperlukan",
                                    color = Color.Gray
                                )
                            }
                        )
                    }

                    // Upload Lampiran Field
                    Column {
                        Text(
                            text = "Upload Lampiran",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                .clickable { filePickerLauncher.launch("*/*") }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.AttachFile,
                                    contentDescription = "Upload",
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = selectedFileName ?: "Browse file",
                                    color = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Submit
            Button(
                onClick = {
                    viewModel.createLogbook(
                        context = context,
                        date = apiDateFormatter.format(selectedDate.time),
                        activity = aktivitas,
                        notes = catatan,
                        attachment = selectedFile
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00A67C),
                    disabledContainerColor = Color(0xFF00A67C).copy(alpha = 0.5f)
                ),
                enabled = aktivitas.isNotBlank() && catatan.isNotBlank()
            ) {
                if (createState is TambahLogbookViewModel.CreateLogbookState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Kirim",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

private fun getFileName(context: Context, uri: Uri): String {
    var fileName = "file"
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }
    return fileName
}