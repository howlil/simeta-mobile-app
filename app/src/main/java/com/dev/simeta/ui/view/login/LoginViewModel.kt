package com.dev.simeta.ui.view.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.LoginResponse
import com.dev.simeta.data.repository.AuthRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                android.util.Log.d("LoginViewModel", "Starting login process for email=$email")
                val result = authRepository.login(email, password)
                if (result.isSuccess) {
                    val loginResponse = result.getOrThrow()
                    android.util.Log.d("LoginViewModel", "Login successful: $loginResponse")
                    loginResponse.data?.token?.let { token ->
                        AuthPreferences.saveAuthToken(context, token)
                        _loginState.value = LoginState.Success(loginResponse)
                    }
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Login failed."
                    android.util.Log.e("LoginViewModel", "Login failed: $error")
                    _loginState.value = LoginState.Error(listOf(error))
                }
            } catch (e: Exception) {
                android.util.Log.e("LoginViewModel", "Unexpected error: ${e.message}")
                _loginState.value = LoginState.Error(listOf("Unexpected error: ${e.message}"))
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
