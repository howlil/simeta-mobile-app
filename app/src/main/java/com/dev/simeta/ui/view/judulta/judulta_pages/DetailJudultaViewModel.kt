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
class DetailJudultaViewModel @Inject constructor(
    private val repository: JudultaRepository
) : ViewModel() {

    sealed class DetailJudultaState {
        object Idle : DetailJudultaState()
        object Loading : DetailJudultaState()
        data class Success(val judulta: JudultaItem) : DetailJudultaState()
        data class Error(val message: String) : DetailJudultaState()
    }

    private val _detailJudultaState = MutableStateFlow<DetailJudultaState>(DetailJudultaState.Idle)
    val detailJudultaState: StateFlow<DetailJudultaState> = _detailJudultaState

    sealed class DeleteState {
        object Idle : DeleteState()
        object Loading : DeleteState()
        data class Success(val message: String) : DeleteState()
        data class Error(val message: String) : DeleteState()
    }

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Idle)
    val deleteState: StateFlow<DeleteState> = _deleteState

    fun fetchJudultaDetail(context: Context, judultaId: String) {
        viewModelScope.launch {
            _detailJudultaState.value = DetailJudultaState.Loading
            try {
                val judulta = repository.getJudultaDetail(context, judultaId)
                _detailJudultaState.value = DetailJudultaState.Success(judulta)
            } catch (e: Exception) {
                _detailJudultaState.value = DetailJudultaState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun deleteJudulta(context: Context, judultaId: String) {
        viewModelScope.launch {
            _deleteState.value = DeleteState.Loading
            try {
                repository.deleteJudulta(context, judultaId)
                _deleteState.value = DeleteState.Success("Judul TA berhasil dihapus")
            } catch (e: Exception) {
                _deleteState.value = DeleteState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}