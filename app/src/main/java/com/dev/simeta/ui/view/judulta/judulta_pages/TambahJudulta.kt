package com.dev.simeta.ui.view.judulta.judulta_pages

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
fun TambahJudulta(
    navController: NavController,
    viewModel: TambahJudultaViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var bidangPeminatan by remember { mutableStateOf("") }
    var dosenPembimbing by remember { mutableStateOf("") }
    var buktiMatkulTA by remember { mutableStateOf<Uri?>(null) }
    var buktiMatkulTAName by remember { mutableStateOf<String?>(null) }
    var buktiLulusKP by remember { mutableStateOf<Uri?>(null) }
    var buktiLulusKPName by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val createState by viewModel.createState.collectAsState()

    val bidangPeminatanList = viewModel.getBidangPeminatan()
    val dosenPembimbingList = viewModel.getDosenPembimbing()

    val filePickerLauncherMatkulTA = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            buktiMatkulTA = it
            buktiMatkulTAName = getFileName(context, it)
        }
    }

    val filePickerLauncherLulusKP = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            buktiLulusKP = it
            buktiLulusKPName = getFileName(context, it)
        }
    }

    LaunchedEffect(createState) {
        when (createState) {
            is TambahJudultaViewModel.CreateJudultaState.Success -> {
                Toast.makeText(
                    context,
                    (createState as TambahJudultaViewModel.CreateJudultaState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack()
            }
            is TambahJudultaViewModel.CreateJudultaState.Error -> {
                Toast.makeText(
                    context,
                    (createState as TambahJudultaViewModel.CreateJudultaState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
    ) {
        DynamicTopBar(
            title = "Tambah Judul TA",
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
                    // Judul Field
                    Column {
                        Text(
                            text = "Judul TA",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
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
                                    "Masukkan judul TA",
                                    color = Color.Gray
                                )
                            }
                        )
                    }

                    // Deskripsi Field
                    Column {
                        Text(
                            text = "Deskripsi TA",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
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
                                    "Tambahkan deskripsi jika diperlukan",
                                    color = Color.Gray
                                )
                            }
                        )
                    }

                    // Bidang Peminatan Dropdown
                    Column {
                        Text(
                            text = "Pilih Bidang Peminatan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        var isDropdownExpanded by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .clickable { isDropdownExpanded = !isDropdownExpanded }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = bidangPeminatan.ifEmpty { "Pilih Bidang Peminatan" },
                                color = if (bidangPeminatan.isEmpty()) Color.Gray else Color.Black
                            )
                        }
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            modifier = Modifier
                                .widthIn(min = 150.dp, max = 300.dp)
                                .background(Color.White)
                        ) {
                            bidangPeminatanList.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        bidangPeminatan = item
                                        isDropdownExpanded = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Dosen Pembimbing Dropdown
                    Column {
                        Text(
                            text = "Pilih Dosen Pembimbing",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        var isDropdownExpanded by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                .clickable { isDropdownExpanded = !isDropdownExpanded }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = dosenPembimbing.ifEmpty { "Pilih Dosen Pembimbing" },
                                color = if (dosenPembimbing.isEmpty()) Color.Gray else Color.Black
                            )
                        }
                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            modifier = Modifier
                                .widthIn(min = 150.dp, max = 300.dp)
                                .background(Color.White)
                        ) {
                            dosenPembimbingList.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        dosenPembimbing = item
                                        isDropdownExpanded = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Bukti Mengambil Matkul TA Field
                    Column {
                        Text(
                            text = "Bukti Mengambil Matkul TA",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                .clickable { filePickerLauncherMatkulTA.launch("application/pdf") }
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
                                    text = buktiMatkulTAName ?: "Browse file",
                                    color = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Bukti Lulus KP dan Matkul Pilihan Field
                    Column {
                        Text(
                            text = "Bukti Lulus KP dan Matkul Pilihan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                .clickable { filePickerLauncherLulusKP.launch("application/pdf") }
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
                                    text = buktiLulusKPName ?: "Browse file",
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
                    viewModel.createJudulta(
                        context = context,
                        title = title,
                        description = description,
                        bidangPeminatan = bidangPeminatan,
                        dosenPembimbing = dosenPembimbing,
                        buktiMatkulTA = buktiMatkulTA,
                        buktiLulusKP = buktiLulusKP
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
                enabled = title.isNotBlank() && description.isNotBlank()
            ) {
                if (createState is TambahJudultaViewModel.CreateJudultaState.Loading) {
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