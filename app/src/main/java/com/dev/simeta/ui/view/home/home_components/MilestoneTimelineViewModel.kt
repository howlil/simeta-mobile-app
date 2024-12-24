package com.dev.simeta.ui.view.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.Milestone
import com.dev.simeta.data.repository.MilestoneRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneTimelineViewModel @Inject constructor(
    private val milestoneRepository: MilestoneRepository
) : ViewModel() {

    private val _milestoneState = MutableStateFlow<MilestoneState>(MilestoneState.Idle)
    val milestoneState: StateFlow<MilestoneState> = _milestoneState

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
                        _milestoneState.value =
                            MilestoneState.Error("Failed to fetch milestones.")
                    }
                } else {
                    _milestoneState.value = MilestoneState.Error("Token is missing.")
                }
            } catch (e: Exception) {
                _milestoneState.value = MilestoneState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    sealed class MilestoneState {
        object Idle : MilestoneState()
        object Loading : MilestoneState()
        data class Success(val milestones: List<Milestone>) : MilestoneState()
        data class Error(val message: String) : MilestoneState()
    }
}
