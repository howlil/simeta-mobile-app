package com.dev.simeta.ui.view.home.home_components

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.simeta.data.model.Mahasiswa
import com.dev.simeta.data.model.UserData
import com.dev.simeta.data.repository.AuthRepository
import com.dev.simeta.helpers.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState: StateFlow<UserState> = _userState

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> = _toastMessage

    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast

    private val _editProfileState = MutableStateFlow<EditProfileState>(EditProfileState.Idle)
    val editProfileState: StateFlow<EditProfileState> = _editProfileState

    fun fetchUserData(context: Context) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token.isNullOrEmpty()) {
                    setToast("Token is missing or invalid.")
                    _userState.value = UserState.Error("Token is missing or invalid.")
                    return@launch
                }

                val result = authRepository.getUser(token)
                if (result.isSuccess) {
                    _userState.value = UserState.Success(result.getOrThrow().data)
                } else {
                    setToast("Failed to fetch user data.")
                    _userState.value = UserState.Error("Failed to fetch user data.")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error: ${e.message}")
                setToast(e.message ?: "Unknown error occurred.")
                _userState.value = UserState.Error(e.message ?: "Unknown error occurred.")
            }
        }
    }


    fun changePassword(context: Context, password: String) {
        if (password.isBlank()) {
            setToast("Password cannot be empty.")
            return
        }
        if (password.length < 6) {
            setToast("Password must be at least 6 characters long.")
            return
        }

        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token.isNullOrEmpty()) {
                    setToast("Token not found.")
                    _userState.value = UserState.Error("Token not found.")
                    return@launch
                }

                val result = authRepository.updatePassword(token, password = password)
                if (result.isSuccess) {
                    setToast("Password successfully changed.")
                    _userState.value = UserState.Success(UserData("", "", "", "", null, null, "", ""))
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Failed to change password."
                    setToast(errorMessage)
                    _userState.value = UserState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error: ${e.message}")
                setToast(e.message ?: "An unknown error occurred.")
                _userState.value = UserState.Error(e.message ?: "An unknown error occurred.")
            }
        }
    }
    private fun setToast(message: String) {
        _toastMessage.value = message
        _showToast.value = true
    }

    fun dismissToast() {
        _showToast.value = false
    }

    private fun Mahasiswa.toUserData(): UserData {
        return UserData(
            id = this.id,
            full_name = this.full_name,
            email = this.email,
            nim = this.nim,
            photoUrl = this.photoUrl,
            fcmToken = this.fcmToken,
            created_at = this.created_at,
            updated_at = this.updated_at
        )
    }

    fun logout(context: Context, onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                AuthPreferences.clearAuthToken(context)
                onLogoutSuccess()
            } catch (e: Exception) {
                Log.e("UserViewModel", "Logout failed: ${e.message}")
            }
        }
    }

    fun updateProfile(context: Context, fullName: String?, email: String?) {
        if (fullName.isNullOrEmpty() && email.isNullOrEmpty()) {
            setToast("Both fields cannot be empty.")
            return
        }

        viewModelScope.launch {
            _editProfileState.value = EditProfileState.Loading
            try {
                val token = AuthPreferences.getAuthToken(context).first()
                if (token.isNullOrEmpty()) {
                    setToast("Token not found.")
                    _editProfileState.value = EditProfileState.Error("Token not found.")
                    return@launch
                }

                val result = authRepository.updateMahasiswa(
                    token = token,
                    fullName = fullName,
                    email = email
                )

                if (result.isSuccess) {
                    // Fetch the latest data
                    fetchUserData(context)
                    _editProfileState.value = EditProfileState.Success
                    setToast("Profile updated successfully.")
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Failed to update profile."
                    _editProfileState.value = EditProfileState.Error(errorMessage)
                    setToast(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("EditProfileViewModel", "Error: ${e.message}")
                _editProfileState.value = EditProfileState.Error(e.message ?: "An unknown error occurred.")
                setToast(e.message ?: "An unknown error occurred.")
            }
        }
    }


    sealed class EditProfileState {
        object Idle : EditProfileState()
        object Loading : EditProfileState()
        object Success : EditProfileState()
        data class Error(val message: String) : EditProfileState()
    }
    sealed class UserState {
        object Idle : UserState()
        object Loading : UserState()
        data class Success(val data: UserData) : UserState()
        data class Error(val message: String) : UserState()
    }
}
