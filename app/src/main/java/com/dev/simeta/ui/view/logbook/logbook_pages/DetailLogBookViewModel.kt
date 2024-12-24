package com.dev.simeta.ui.view.logbook.logbook_pages

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.DetailLogBook
import com.dev.simeta.data.repository.LogbookRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailLogbookViewModel @Inject constructor(
    private val logbookRepository: LogbookRepository
) : ViewModel() {

    private val _detailLogbookState = MutableStateFlow<DetailLogbookState>(DetailLogbookState.Idle)
    val detailLogbookState: StateFlow<DetailLogbookState> = _detailLogbookState

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Idle)
    val deleteState: StateFlow<DeleteState> = _deleteState
    fun fetchLogbookDetail(context: Context, logbookId: String) {
        viewModelScope.launch {
            _detailLogbookState.value = DetailLogbookState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = logbookRepository.getLogbookDetail(token, logbookId)
                    if (result.isSuccess) {
                        val logbook = result.getOrThrow() // Objek DetailLogBook
                        _detailLogbookState.value = DetailLogbookState.Success(logbook)
                    } else {
                        _detailLogbookState.value =
                            DetailLogbookState.Error("Failed to fetch logbook detail.")
                    }
                } else {
                    _detailLogbookState.value = DetailLogbookState.Error("Token is missing.")
                }
            } catch (e: Exception) {
                _detailLogbookState.value =
                    DetailLogbookState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    fun deleteLogbook(context: Context, logbookId: String) {
        viewModelScope.launch {
            _deleteState.value = DeleteState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = logbookRepository.deleteLogbook(token, logbookId)
                    if (result.isSuccess) {
                        _deleteState.value = DeleteState.Success(result.getOrNull()?.messages ?: "Logbook deleted successfully")
                    } else {
                        _deleteState.value = DeleteState.Error(result.exceptionOrNull()?.message ?: "Failed to delete logbook")
                    }
                } else {
                    _deleteState.value = DeleteState.Error("Token is missing")
                }
            } catch (e: Exception) {
                _deleteState.value = DeleteState.Error("Unexpected error: ${e.message}")
            }
        }
    }
    sealed class DeleteState {
        object Idle : DeleteState()
        object Loading : DeleteState()
        data class Success(val message: String) : DeleteState()
        data class Error(val message: String) : DeleteState()
    }
    sealed class DetailLogbookState {
        object Idle : DetailLogbookState()
        object Loading : DetailLogbookState()
        data class Success(val logbook: DetailLogBook) : DetailLogbookState()
        data class Error(val message: String) : DetailLogbookState()
    }
}