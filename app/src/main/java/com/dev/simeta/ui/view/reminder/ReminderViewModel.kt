package com.dev.simeta.ui.view.reminder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.Reminder
import com.dev.simeta.data.repository.ReminderRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {

    private val _reminderState = MutableStateFlow<ReminderState>(ReminderState.Idle)
    val reminderState: StateFlow<ReminderState> = _reminderState

    fun fetchReminders(context: Context) {
        viewModelScope.launch {
            _reminderState.value = ReminderState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = repository.getReminders(token)
                    if (result.isSuccess) {
                        _reminderState.value = ReminderState.Success(result.getOrThrow().data)
                    } else {
                        _reminderState.value = ReminderState.Error("Failed to fetch reminders.")
                    }
                } else {
                    _reminderState.value = ReminderState.Error("Token not found.")
                }
            } catch (e: Exception) {
                _reminderState.value = ReminderState.Error(e.message ?: "Unexpected error.")
            }
        }
    }

    sealed class ReminderState {
        object Idle : ReminderState()
        object Loading : ReminderState()
        data class Success(val reminders: List<Reminder>) : ReminderState()
        data class Error(val message: String) : ReminderState()
    }
}
