package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.ServiceLocator
import com.example.mixandmealapp.data.SessionRepository
import com.example.mixandmealapp.data.UserProfile
import com.example.mixandmealapp.data.UserRepository
import kotlinx.coroutines.launch

data class AccountUiState(
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)

class AccountViewModel(
    private val userRepo: UserRepository = ServiceLocator.userRepository,
    private val sessionRepo: SessionRepository = ServiceLocator.sessionRepository
) : ViewModel() {
    var uiState by mutableStateOf(AccountUiState())
        private set

    init { load() }

    private fun load() {
        viewModelScope.launch {
            try {
                val profile = userRepo.getProfile()
                uiState = uiState.copy(name = profile.name, email = profile.email, avatarUrl = profile.avatarUrl)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }

    fun onNameChange(value: String) { uiState = uiState.copy(name = value, saved = false) }
    fun onEmailChange(value: String) { uiState = uiState.copy(email = value, saved = false) }
    fun setAvatar(url: String?) { uiState = uiState.copy(avatarUrl = url, saved = false) }

    fun save() {
        uiState = uiState.copy(isSaving = true, error = null)
        viewModelScope.launch {
            try {
                userRepo.saveProfile(UserProfile(name = uiState.name, email = uiState.email, avatarUrl = uiState.avatarUrl))
                uiState = uiState.copy(isSaving = false, saved = true)
            } catch (t: Throwable) {
                uiState = uiState.copy(isSaving = false, error = t.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                sessionRepo.clear()
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }
}
