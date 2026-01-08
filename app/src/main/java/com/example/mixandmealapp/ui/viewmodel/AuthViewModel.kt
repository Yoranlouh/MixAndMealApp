package com.example.mixandmealapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.models.responses.AuthResponse
import com.example.mixandmealapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: TokenRepository,
    private val homeViewModel: HomeViewModel,
    private val userRepo: UserRepository // <-- ADD THIS
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                // Perform login
                Log.d("AuthViewModel", "Attempting login for $email")
                // 2. Use the injected userRepo, not a new instance
                val response = userRepo.login(email, password) // <-- CHANGE THIS

                // Save token
                repo.setToken(response.token)
                Log.d("AuthViewModel", "Token saved, updating HomeViewModel role")

                // Update HomeViewModel immediately with the new token
                homeViewModel.authenticateRole(response.token)

                _uiState.value = AuthUiState.Success(response)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login failed", e)
                _uiState.value = AuthUiState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                Log.d("AuthViewModel", "Attempting registration for $username, $email")
                // Call the repository (This will now work)
                val response = userRepo.register(username, email, password)

                // After successful registration, save token and authenticate
                repo.setToken(response.token)
                Log.d("AuthViewModel", "Token saved, updating HomeViewModel role")
                homeViewModel.authenticateRole(response.token)

                _uiState.value = AuthUiState.Success(response)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration failed", e)
                _uiState.value = AuthUiState.Error(e.message ?: "Registration failed")
            }
        }
    }
}


sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val auth: AuthResponse) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
