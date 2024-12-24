package com.dev.simeta.ui.view.logbook.logbook_pages

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.repository.LogbookRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

// ui/viewmodel/TambahLogbookViewModel.kt
@HiltViewModel
class TambahLogbookViewModel @Inject constructor(
    private val repository: LogbookRepository
) : ViewModel() {

    private val _createState = MutableStateFlow<CreateLogbookState>(CreateLogbookState.Idle)
    val createState: StateFlow<CreateLogbookState> = _createState.asStateFlow()

    fun createLogbook(
        context: Context,
        date: String,
        activity: String,
        notes: String,
        attachment: Uri?
    ) {
        viewModelScope.launch {
            _createState.value = CreateLogbookState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = repository.createLogbook(
                        token = token,
                        date = date,
                        activity = activity,
                        notes = notes,
                        attachment = attachment,
                        context = context
                    )
                    if (result.isSuccess) {
                        _createState.value = CreateLogbookState.Success(result.getOrNull()?.messages ?: "Success")
                    } else {
                        _createState.value = CreateLogbookState.Error(
                            result.exceptionOrNull()?.message ?: "Unknown error occurred"
                        )
                    }
                } else {
                    _createState.value = CreateLogbookState.Error("Token not found")
                }
            } catch (e: Exception) {
                _createState.value = CreateLogbookState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class CreateLogbookState {
        object Idle : CreateLogbookState()
        object Loading : CreateLogbookState()
        data class Success(val message: String) : CreateLogbookState()
        data class Error(val message: String) : CreateLogbookState()
    }
}