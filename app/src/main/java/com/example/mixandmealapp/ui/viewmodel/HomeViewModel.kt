package com.example.mixandmealapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.models.responses.RoleResponse
import com.example.mixandmealapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Base64

data class HomeUiState(
    val featured: List<String> = emptyList(),
    val popular: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val repo: TokenRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _role = MutableStateFlow<RoleResponse>(RoleResponse("GUEST", "", "Guest"))
    val role: StateFlow<RoleResponse> = _role.asStateFlow()

    init {
        authenticateRole()
    }

    fun authenticateRole(token: String? = null) {
        viewModelScope.launch {
            try {
                // 1. Get token
                val tokenToUse = token ?: repo.getTokenOrDefault()

                // 2. CHECK: Use isNullOrBlank() to cover null, empty string "", and whitespace " "
                if (tokenToUse.isNullOrBlank()) {
                    Log.d("HomeViewModel", "No token found, defaulting to GUEST")
                    _role.value = RoleResponse("GUEST", "", "Guest")
                    return@launch
                }

                Log.d("HomeViewModel", "Authenticating with token...")

                var roleFound: RoleResponse? = null

                // 3. Try backend check
                try {
                    val response = userRepo.checkRole(tokenToUse)
                    if (response != null) {
                        roleFound = response
                        Log.d("HomeViewModel", "Role determined: $roleFound")
                        _role.value = roleFound
                    }
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Backend checkRole failed", e)
                    // OPTIONAL: If error is 401 (Unauthorized), clear token immediately?
                    // For now, let fallback handle it.
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error in authenticateRole", e)
                _role.value = RoleResponse("GUEST", "", "Guest")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.clearToken()
            _role.value = RoleResponse("GUEST", "", "Guest")
        }
    }
}
