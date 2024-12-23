package com.dev.simeta.ui.view.home.home_components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.DashboardData
import com.dev.simeta.data.repository.AuthRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<DashboardState>(DashboardState.Idle)
    val dashboardState: StateFlow<DashboardState> = _dashboardState

    fun fetchDashboardData(context: android.content.Context) {
        viewModelScope.launch {
            _dashboardState.value = DashboardState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = authRepository.getDashboard(token)
                    if (result.isSuccess) {
                        _dashboardState.value = DashboardState.Success(result.getOrThrow().data)
                    } else {
                        _dashboardState.value = DashboardState.Error("Failed to fetch dashboard data.")
                    }
                } else {
                    _dashboardState.value = DashboardState.Error("Token not found.")
                }
            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error(e.message ?: "Unexpected error.")
            }
        }
    }



    sealed class DashboardState {
        object Idle : DashboardState()
        object Loading : DashboardState()
        data class Success(val data: DashboardData) : DashboardState()
        data class Error(val message: String) : DashboardState()
    }
}
