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

    private val _role = MutableStateFlow<RoleResponse>(RoleResponse("Guest"))
    val role: StateFlow<RoleResponse> = _role.asStateFlow()

    init {
        authenticateRole()
    }

    fun authenticateRole(token: String? = null) {
        viewModelScope.launch {
            try {
                val tokenToUse = token ?: repo.getTokenOrDefault()

                if (tokenToUse.isNotEmpty()) {
                    Log.d("HomeViewModel", "Authenticating with token...")
                    
                    var roleFound: String? = null
                    
                    // 1. Try backend
                    try {
                        val userRole = userRepo.checkRole(tokenToUse)
                        if (userRole != null) {
                            roleFound = userRole.role
                        }
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Backend checkRole failed", e)
                    }

                    // 2. Fallback to JWT decoding if backend failed or returned nothing
                    if (roleFound == null) {
                         Log.d("HomeViewModel", "Attempting JWT decode fallback")
                         roleFound = getRoleFromToken(tokenToUse)
                    }

                    if (roleFound != null) {
                        Log.d("HomeViewModel", "Role determined: $roleFound")
                        _role.value = RoleResponse(roleFound)
                    } else {
                        Log.w("HomeViewModel", "Could not determine role, defaulting to GUEST")
                        _role.value = RoleResponse("GUEST")
                    }
                } else {
                    Log.d("HomeViewModel", "No token found, defaulting to GUEST")
                    _role.value = RoleResponse("GUEST")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error authenticating role", e)
                _role.value = RoleResponse("GUEST")
            }
        }
    }

    private fun getRoleFromToken(token: String): String? {
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                // Decode payload
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                Log.d("HomeViewModel", "JWT Payload: $payload")

                // Try various common keys for role
                val keys = listOf("role", "roles", "authorities", "scope", "scp", "permission")
                var foundValue: String? = null
                
                for (key in keys) {
                    // Regex to find "key":"VALUE" or "key":["VALUE"]
                    val regex = "\"$key\"\\s*:\\s*\"?([^\",\\]]+)\"?".toRegex(RegexOption.IGNORE_CASE)
                    val match = regex.find(payload)
                    if (match != null) {
                        foundValue = match.groupValues[1]
                        break
                    }
                }
                foundValue
            } else null
        } catch (e: Exception) {
            Log.e("HomeViewModel", "JWT decode failed", e)
            null
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.clearToken()
            _role.value = RoleResponse("GUEST")
        }
    }
}
