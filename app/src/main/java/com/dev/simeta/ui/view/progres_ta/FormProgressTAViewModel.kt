package com.dev.simeta.ui.view.progress_ta

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.CreateProgressRequest
import com.dev.simeta.data.model.Milestone
import com.dev.simeta.data.repository.MilestoneRepository
import com.dev.simeta.data.repository.ProgressRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormProgressTAViewModel @Inject constructor(
    private val repository: ProgressRepository,
    private val milestoneRepository: MilestoneRepository

) : ViewModel() {

    private val _milestoneState = MutableStateFlow<MilestoneState>(MilestoneState.Idle)
    val milestoneState: StateFlow<MilestoneState> = _milestoneState

    private val _createProgressState = MutableStateFlow<CreateProgressState>(CreateProgressState.Idle)
    val createProgressState: StateFlow<CreateProgressState> = _createProgressState

    fun fetchMilestones(context: Context) {
        viewModelScope.launch {
            _milestoneState.value = MilestoneState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = milestoneRepository.getMilestones(token)
                    if (result.isSuccess) {
                        _milestoneState.value = MilestoneState.Success(result.getOrThrow().data)
                    } else {
                        _milestoneState.value = MilestoneState.Error("Failed to fetch milestones.")
                    }
                } else {
                    _milestoneState.value = MilestoneState.Error("Token not found.")
                }
            } catch (e: Exception) {
                _milestoneState.value = MilestoneState.Error(e.message ?: "Unexpected error.")
            }
        }
    }

    fun createProgress(context: Context, request: CreateProgressRequest) {
        viewModelScope.launch {
            _createProgressState.value = CreateProgressState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = repository.createProgress(token, request)
                    if (result.isSuccess) {
                        _createProgressState.value = CreateProgressState.Success(result.getOrThrow().messages)
                    } else {
                        _createProgressState.value = CreateProgressState.Error("Failed to create progress.")
                    }
                } else {
                    _createProgressState.value = CreateProgressState.Error("Token not found.")
                }
            } catch (e: Exception) {
                _createProgressState.value = CreateProgressState.Error(e.message ?: "Unexpected error.")
            }
        }
    }

    sealed class MilestoneState {
        object Idle : MilestoneState()
        object Loading : MilestoneState()
        data class Success(val milestones: List<Milestone>) : MilestoneState()
        data class Error(val message: String) : MilestoneState()
    }

    sealed class CreateProgressState {
        object Idle : CreateProgressState()
        object Loading : CreateProgressState()
        data class Success(val message: String) : CreateProgressState()
        data class Error(val message: String) : CreateProgressState()
    }
}
