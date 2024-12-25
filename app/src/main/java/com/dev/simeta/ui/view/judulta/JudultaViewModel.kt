package com.dev.simeta.ui.view.judulta

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.repository.JudultaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JudultaViewModel @Inject constructor(
    private val repository: JudultaRepository
) : ViewModel() {

    sealed class JudultaState {
        object Idle : JudultaState()
        object Loading : JudultaState()
        data class Success(val data: List<JudultaItem>) : JudultaState()
        data class Error(val message: String) : JudultaState()
    }

    private val _judultaState = MutableStateFlow<JudultaState>(JudultaState.Idle)
    val judultaState: StateFlow<JudultaState> = _judultaState

    fun fetchJudulta(context: Context) {
        viewModelScope.launch {
            _judultaState.value = JudultaState.Loading
            try {
                val data = repository.getJudultaItems(context)
                _judultaState.value = JudultaState.Success(data)
            } catch (e: Exception) {
                _judultaState.value = JudultaState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}