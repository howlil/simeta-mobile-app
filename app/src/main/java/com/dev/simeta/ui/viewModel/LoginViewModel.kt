package com.dev.simeta.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.repository.AuthRepository
import com.dev.simeta.data.model.LoginResponse
import com.dev.simeta.helpers.parseErrorMessages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val result = withTimeout(5000) { authRepository.login(email, password) }
                if (result.isSuccess) {
                    val loginResponse = result.getOrThrow()
                    android.util.Log.d("LoginViewModel", "Login successful: ${loginResponse}")
                    _loginState.value = LoginState.Success(loginResponse)
                } else {
                    val errorMessages = result.exceptionOrNull()?.message?.let {
                        parseErrorMessages(it)
                    } ?: listOf("Unknown error occurred.")
                    android.util.Log.e("LoginViewModel", "Login failed: $errorMessages")
                    _loginState.value = LoginState.Error(errorMessages)
                }
            } catch (e: Exception) {
                android.util.Log.e("LoginViewModel", "Unexpected error: ${e.message}")
                _loginState.value = LoginState.Error(listOf("Unexpected error occurred."))
            }
        }
    }



    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val response: LoginResponse) : LoginState()
        data class Error(val messages: List<String>) : LoginState()
    }

}
