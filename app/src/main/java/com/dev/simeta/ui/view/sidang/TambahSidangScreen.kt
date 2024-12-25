package com.dev.simeta.ui.view.sidang

import android.app.TimePickerDialog
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahSidangScreen(
    navController: NavController,
    viewModel: SidangViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedTime by remember { mutableStateOf("") }
    var selectedFile by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd MMMM yyyy", Locale("id")) }
    val apiDateFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale("id")) }
    val context = LocalContext.current
    val sidangState by viewModel.createSidangState.collectAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFile = it
            selectedFileName = getFileName(context, it)
        }
    }

    // Handle success and navigation
    LaunchedEffect(sidangState) {
        if (sidangState is SidangViewModel.CreateSidangState.Success) {
            Toast.makeText(context, "Sidang berhasil diajukan!", Toast.LENGTH_SHORT).show()
            navController.popBackStack() // Navigate back to the previous screen
        } else if (sidangState is SidangViewModel.CreateSidangState.Error) {
            val errorMessage = (sidangState as SidangViewModel.CreateSidangState.Error).message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
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

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                showTimePicker = false
            },
            selectedDate.get(Calendar.HOUR_OF_DAY),
            selectedDate.get(Calendar.MINUTE),
            true
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        DynamicTopBar(
            title = "Tambah Sidang",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )

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
                // Date Picker
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

                // Time Picker
                Column {
                    Text(
                        text = "Waktu",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                            .clickable { showTimePicker = true }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Clock",
                            tint = Color(0xFF00A67C)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (selectedTime.isBlank()) "Pilih Waktu" else selectedTime,
                            color = Color.DarkGray
                        )
                    }
                }

                // Upload Document Field
                Column {
                    Text(
                        text = "Upload Dokumen",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                            .clickable { filePickerLauncher.launch("application/pdf") }
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

        // Submit Button
        Button(
            onClick = {
                if (selectedFile != null && selectedTime.isNotBlank()) {
                    val file = getFileFromUri(context, selectedFile!!)
                    if (file != null) {
                        val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
                        val filePart = MultipartBody.Part.createFormData("dokumen", file.name, requestBody)

                        viewModel.createSidang(
                            context = context,
                            date = apiDateFormatter.format(selectedDate.time).toRequestBody("text/plain".toMediaTypeOrNull()),
                            time = selectedTime.toRequestBody("text/plain".toMediaTypeOrNull()),
                            dokumen = filePart
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00A67C),
                disabledContainerColor = Color(0xFF00A67C).copy(alpha = 0.5f)
            ),
            enabled = selectedFile != null && selectedTime.isNotBlank()
        ) {
            if (sidangState is SidangViewModel.CreateSidangState.Loading) {
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



private fun getFileName(context: Context, uri: Uri): String {
    var fileName = "file"
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }
    return fileName
}

private fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val fileName = getFileName(context, uri)
        val tempFile = File(context.cacheDir, fileName)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
