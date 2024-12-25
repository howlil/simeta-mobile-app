package com.dev.simeta.ui.view.judulta.judulta_pages

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.repository.JudultaRepository
import com.dev.simeta.ui.view.judulta.JudultaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditJudultaViewModel @Inject constructor(
    private val repository: JudultaRepository
) : ViewModel() {

    private val _detailJudultaState = MutableStateFlow<DetailJudultaState>(DetailJudultaState.Idle)
    val detailJudultaState: StateFlow<DetailJudultaState> = _detailJudultaState

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState

    fun fetchJudultaDetail(context: Context, judultaId: String) {
        viewModelScope.launch {
            _detailJudultaState.value = DetailJudultaState.Loading
            try {
                val judulta = repository.getJudultaDetail(context, judultaId)
                _detailJudultaState.value = DetailJudultaState.Success(judulta)
            } catch (e: Exception) {
                _detailJudultaState.value = DetailJudultaState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateJudulta(context: Context, id: String, title: String, description: String) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            try {
                val success = repository.updateJudulta(context, id, title, description)
                if (success) {
                    _updateState.value = UpdateState.Success("Judul TA berhasil diperbarui")
                } else {
                    _updateState.value = UpdateState.Error("Gagal memperbarui Judul TA")
                }
            } catch (e: Exception) {
                _updateState.value = UpdateState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class DetailJudultaState {
        object Idle : DetailJudultaState()
        object Loading : DetailJudultaState()
        data class Success(val judulta: JudultaItem) : DetailJudultaState()
        data class Error(val message: String) : DetailJudultaState()
    }

    sealed class UpdateState {
        object Idle : UpdateState()
        object Loading : UpdateState()
        data class Success(val message: String) : UpdateState()
        data class Error(val message: String) : UpdateState()
    }
}