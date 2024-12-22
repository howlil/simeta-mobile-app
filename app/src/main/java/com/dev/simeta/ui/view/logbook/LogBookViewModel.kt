package com.dev.simeta.ui.view.logbook

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.Logbook
import com.dev.simeta.data.repository.LogbookRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogbookViewModel @Inject constructor(
    private val logbookRepository: LogbookRepository
) : ViewModel() {

    private val _logbookState = MutableStateFlow<LogbookState>(LogbookState.Idle)
    val logbookState: StateFlow<LogbookState> = _logbookState

    fun fetchLogbooks(context: Context) {
        viewModelScope.launch {
            _logbookState.value = LogbookState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = logbookRepository.getLogbooks(token)
                    if (result.isSuccess) {
                        _logbookState.value = LogbookState.Success(result.getOrThrow().data)
                    } else {
                        _logbookState.value = LogbookState.Error("Failed to fetch logbooks.")
                    }
                } else {
                    _logbookState.value = LogbookState.Error("Token is missing.")
                }
            } catch (e: Exception) {
                _logbookState.value = LogbookState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    sealed class LogbookState {
        object Idle : LogbookState()
        object Loading : LogbookState()
        data class Success(val data: List<Logbook>) : LogbookState()
        data class Error(val message: String) : LogbookState()
    }
}
