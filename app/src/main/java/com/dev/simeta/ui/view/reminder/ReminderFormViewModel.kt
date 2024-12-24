package com.dev.simeta.ui.view.reminder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.CreateReminderRequest
import com.dev.simeta.data.repository.ReminderRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderFormViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _createReminderState = MutableStateFlow<CreateReminderState>(CreateReminderState.Idle)
    val createReminderState: StateFlow<CreateReminderState> = _createReminderState

    fun createReminder(context: Context, request: CreateReminderRequest) {
        viewModelScope.launch {
            _createReminderState.value = CreateReminderState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = reminderRepository.createReminder(token, request)
                    if (result.isSuccess) {
                        _createReminderState.value = CreateReminderState.Success(result.getOrThrow().messages)
                    } else {
                        _createReminderState.value = CreateReminderState.Error("Failed to create reminder.")
                    }
                } else {
                    _createReminderState.value = CreateReminderState.Error("Token not found.")
                }
            } catch (e: Exception) {
                _createReminderState.value = CreateReminderState.Error(e.message ?: "Unexpected error.")
            }
        }
    }

    sealed class CreateReminderState {
        object Idle : CreateReminderState()
        object Loading : CreateReminderState()
        data class Success(val message: String) : CreateReminderState()
        data class Error(val message: String) : CreateReminderState()
    }
}
