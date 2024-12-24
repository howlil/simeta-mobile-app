package com.dev.simeta.ui.view.progress_ta

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.Milestone
import com.dev.simeta.data.model.Progress
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
class RiwayatProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val milestoneRepository: MilestoneRepository
) : ViewModel() {

    private val _milestoneState = MutableStateFlow<MilestoneState>(MilestoneState.Idle)
    val milestoneState: StateFlow<MilestoneState> = _milestoneState

    private val _progressState = MutableStateFlow<ProgressState>(ProgressState.Idle)
    val progressState: StateFlow<ProgressState> = _progressState

    // Fetch milestones
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

    // Fetch progress based on milestone ID
    fun fetchProgress(context: Context, milestoneId: String? = null) {
        viewModelScope.launch {
            _progressState.value = ProgressState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = progressRepository.getProgress(token, milestoneId)
                    if (result.isSuccess) {
                        _progressState.value = ProgressState.Success(result.getOrThrow().data)
                    } else {
                        _progressState.value = ProgressState.Error(
                            result.exceptionOrNull()?.message ?: "Failed to fetch progress data."
                        )
                    }
                } else {
                    _progressState.value = ProgressState.Error("Token is missing.")
                }
            } catch (e: Exception) {
                _progressState.value = ProgressState.Error("Unexpected error: ${e.message}")
            }
        }
    }


    // Milestone states
    sealed class MilestoneState {
        object Idle : MilestoneState()
        object Loading : MilestoneState()
        data class Success(val milestones: List<Milestone>) : MilestoneState()
        data class Error(val message: String) : MilestoneState()
    }

    // Progress states
    sealed class ProgressState {
        object Idle : ProgressState()
        object Loading : ProgressState()
        data class Success(val progressList: List<Progress>) : ProgressState()
        data class Error(val message: String) : ProgressState()
    }
}
