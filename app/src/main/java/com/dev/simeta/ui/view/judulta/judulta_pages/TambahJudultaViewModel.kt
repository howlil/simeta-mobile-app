package com.dev.simeta.ui.view.judulta.judulta_pages

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.repository.JudultaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TambahJudultaViewModel @Inject constructor(
    private val repository: JudultaRepository
) : ViewModel() {

    sealed class CreateJudultaState {
        object Idle : CreateJudultaState()
        object Loading : CreateJudultaState()
        data class Success(val message: String) : CreateJudultaState()
        data class Error(val message: String) : CreateJudultaState()
    }

    private val _createState = MutableStateFlow<CreateJudultaState>(CreateJudultaState.Idle)
    val createState: StateFlow<CreateJudultaState> = _createState

    fun createJudulta(
        context: Context,
        title: String,
        description: String,
        bidangPeminatan: String,
        dosenPembimbing: String,
        buktiMatkulTA: Uri?,
        buktiLulusKP: Uri?
    ) {
        viewModelScope.launch {
            _createState.value = CreateJudultaState.Loading
            try {
                val success = repository.createJudulta(
                    context,
                    title,
                    description,
                    bidangPeminatan,
                    dosenPembimbing,
                    buktiMatkulTA,
                    buktiLulusKP
                )
                if (success) {
                    _createState.value = CreateJudultaState.Success("Judul TA berhasil ditambahkan")
                } else {
                    _createState.value = CreateJudultaState.Error("Gagal menambahkan Judul TA")
                }
            } catch (e: Exception) {
                _createState.value = CreateJudultaState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun getBidangPeminatan(): List<String> {
        return repository.getBidangPeminatan()
    }

    fun getDosenPembimbing(): List<String> {
        return repository.getDosenPembimbing()
    }
}