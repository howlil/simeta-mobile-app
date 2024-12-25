package com.dev.simeta.ui.view.user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {

    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    fun changePassword(oldPassword: String, newPassword: String, context: Context) {
        viewModelScope.launch {
            // Add your password change logic here
            // For example, make a network request to change the password

            // On success
            _toastMessage.value = "Password telah diubah"
            _showToast.value = true

            // On failure
            // _toastMessage.value = "Gagal mengubah password"
            // _showToast.value = true
        }
    }

    fun dismissToast() {
        _showToast.value = false
    }
}