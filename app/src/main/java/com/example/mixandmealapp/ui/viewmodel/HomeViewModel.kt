package com.example.mixandmealapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.RecipesRepository
import com.example.mixandmealapp.data.ServiceLocator
import com.example.mixandmealapp.models.entries.TokenClaim
import com.example.mixandmealapp.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val featured: List<String> = emptyList(),
    val popular: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(

) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun authenticateRole(
        name: String,
        value: String

        ) {
        val token = TokenClaim(name, value)
        viewModelScope.launch {

            try {
                val response = UserRepository().checkRole(token)
//                _uiState.value = AuthUiState.Success(response)
            } catch (e: Exception) {
                Log.d("HomeViewModel", "Error authenticating role: ${e.message}")

            }
        }
    }
}