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

    fun authenticateRole() {
        Log.d("HomeViewModel", "🔥 authenticateRole() AANGEROEPEN!")  // ← BINNENKOMST

        viewModelScope.launch {
            Log.d("HomeViewModel", "🚀 COROUTINE GESTART!")  // ← CRUCIAAL!

            try {
                Log.d("HomeViewModel", "📝 first try: ${_role.value}")

                val token = repo.getTokenOrDefault()
                Log.d("HomeViewModel", "🔑 Token: $token")

                val userRole = userRepo.checkRole(token)
                Log.d("HomeViewModel", "👤 UserRole: $userRole")

                _role.value = userRole ?: RoleResponse("GUEST")
                Log.d("HomeViewModel", "✅ SUCCESS: ${_role.value}")

            } catch (e: Exception) {
                Log.e("HomeViewModel", "💥 ERROR: ${e.message}", e)
                _role.value = RoleResponse("GUEST")
                Log.d("HomeViewModel", "❌ EXCEPTION SET: ${_role.value}")
            }
        }

        Log.d("HomeViewModel", "🏁 authenticateRole() EIND (buiten coroutine)")
    }
}
