package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.AuthRepository
import com.example.mixandmealapp.data.ServiceLocator
import com.example.mixandmealapp.data.SessionRepository
import com.example.mixandmealapp.data.SettingsRepository
import com.example.mixandmealapp.repository.UserRepository
import kotlinx.coroutines.launch

// Login
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class TestLoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(v: String) {
        uiState = uiState.copy(email = v)
    }

    fun onPasswordChange(v: String) {
        uiState = uiState.copy(password = v)
    }

    fun login(email: String, password: String) {
        uiState = uiState.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Use your UserRepository directly
                val userRepository = UserRepository()
                val token = userRepository.testLogin(email, password)

                if (token != null) {
                    uiState = uiState.copy(isLoading = false, success = true)
                } else {
                    uiState = uiState.copy(isLoading = false, error = "Login failed")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.localizedMessage ?: "Network error")
            }
        }
    }
}




class LoginViewModel(
    private val auth: AuthRepository = ServiceLocator.authRepository,
    private val session: SessionRepository = ServiceLocator.sessionRepository
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(v: String) { uiState = uiState.copy(email = v) }
    fun onPasswordChange(v: String) { uiState = uiState.copy(password = v) }
    fun login() {
        uiState = uiState.copy(isLoading = true, error = null, success = false)
        viewModelScope.launch {
            try {
                val result = auth.login(uiState.email, uiState.password)
                if (result.success && result.token != null) {
                    session.saveToken(result.token)
                    uiState = uiState.copy(isLoading = false, success = true)
                } else {
                    uiState = uiState.copy(isLoading = false, error = result.error ?: "Login failed")
                }
            } catch (t: Throwable) {
                uiState = uiState.copy(isLoading = false, error = t.message)
            }
        }
    }
}

// Register
data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class RegisterViewModel(
    private val auth: AuthRepository = ServiceLocator.authRepository,
    private val session: SessionRepository = ServiceLocator.sessionRepository
) : ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onNameChange(v: String) { uiState = uiState.copy(name = v) }
    fun onEmailChange(v: String) { uiState = uiState.copy(email = v) }
    fun onPasswordChange(v: String) { uiState = uiState.copy(password = v) }
    fun onConfirmPasswordChange(v: String) { uiState = uiState.copy(confirmPassword = v) }
    fun register() {
        uiState = uiState.copy(isLoading = true, error = null, success = false)
        viewModelScope.launch {
            try {
                if (uiState.password != uiState.confirmPassword) {
                    uiState = uiState.copy(isLoading = false, error = "Passwords do not match")
                    return@launch
                }
                val result = auth.register(uiState.name, uiState.email, uiState.password)
                if (result.success && result.token != null) {
                    session.saveToken(result.token)
                    uiState = uiState.copy(isLoading = false, success = true)
                } else {
                    uiState = uiState.copy(isLoading = false, error = result.error ?: "Registration failed")
                }
            } catch (t: Throwable) {
                uiState = uiState.copy(isLoading = false, error = t.message)
            }
        }
    }
}

// Splash / session
data class LoginSplashUiState(
    val isChecking: Boolean = true,
    val isLoggedIn: Boolean = false,
    val requiresPrivacy: Boolean = false,
    val error: String? = null
)

class LoginSplashViewModel(
    private val session: SessionRepository = ServiceLocator.sessionRepository,
    private val settings: SettingsRepository = ServiceLocator.settingsRepository
) : ViewModel() {
    var uiState by mutableStateOf(LoginSplashUiState())
        private set

    fun checkSession() {
        uiState = uiState.copy(isChecking = true, error = null)
        viewModelScope.launch {
            try {
                val loggedIn = session.isLoggedIn()
                val privacy = settings.isPrivacyAccepted()
                uiState = uiState.copy(isChecking = false, isLoggedIn = loggedIn, requiresPrivacy = !privacy)
            } catch (t: Throwable) {
                uiState = uiState.copy(isChecking = false, error = t.message)
            }
        }
    }
}
