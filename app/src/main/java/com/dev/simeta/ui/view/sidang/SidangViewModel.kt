package com.dev.simeta.ui.view.sidang

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.Sidang
import com.dev.simeta.data.model.SidangResponseCreate
import com.dev.simeta.data.repository.SidangRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SidangViewModel @Inject constructor(
    private val sidangRepository: SidangRepository
) : ViewModel() {

    private val _sidangState = MutableStateFlow<SidangState>(SidangState.Idle)
    val sidangState: StateFlow<SidangState> = _sidangState

    private val _createSidangState = MutableStateFlow<CreateSidangState>(CreateSidangState.Idle)
    val createSidangState: StateFlow<CreateSidangState> = _createSidangState

    fun fetchSidang(context: Context) {
        viewModelScope.launch {
            _sidangState.value = SidangState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = sidangRepository.getSidang(token)
                    if (result.isSuccess) {
                        _sidangState.value = SidangState.Success(result.getOrThrow().data)
                    } else {
                        _sidangState.value = SidangState.Error("Failed to fetch sidang data.")
                    }
                } else {
                    _sidangState.value = SidangState.Error("Token not found.")
                }
            } catch (e: Exception) {
                _sidangState.value = SidangState.Error(e.message ?: "Unexpected error.")
            }
        }
    }

    fun createSidang(
        context: Context,
        date: RequestBody,
        time: RequestBody,
        dokumen: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _createSidangState.value = CreateSidangState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token != null) {
                    val result = sidangRepository.createSidang(token, date, time, dokumen)
                    if (result.isSuccess) {
                        _createSidangState.value = CreateSidangState.Success(result.getOrThrow())
                    } else {
                        _createSidangState.value = CreateSidangState.Error("Failed to create Sidang.")
                    }
                } else {
                    _createSidangState.value = CreateSidangState.Error("Token not found.")
                }
            } catch (e: Exception) {
                _createSidangState.value = CreateSidangState.Error(e.message ?: "Unexpected error.")
            }
        }
    }



    sealed class SidangState {
        object Idle : SidangState()
        object Loading : SidangState()
        data class Success(val data: List<Sidang>) : SidangState()
        data class Error(val message: String) : SidangState()
    }

    sealed class CreateSidangState {
        object Idle : CreateSidangState()
        object Loading : CreateSidangState()
        data class Success(val data: SidangResponseCreate) : CreateSidangState()
        data class Error(val message: String) : CreateSidangState()
    }
}
