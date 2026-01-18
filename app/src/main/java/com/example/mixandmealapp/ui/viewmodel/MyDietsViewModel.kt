package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.entries.UserDietEntry
import com.example.mixandmealapp.models.entries.UserFridgeEntry
import com.example.mixandmealapp.models.requests.DietsIDRequest
import com.example.mixandmealapp.models.requests.IngredientIDRequest
import com.example.mixandmealapp.repository.FridgeRepository
import com.example.mixandmealapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DietsUiState(
    val items: List<DietEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MyDietViewModel(
    private val repo: UserRepository = UserRepository(),
    private val tokenRepo: TokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DietsUiState())
    val uiState: StateFlow<DietsUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val list = repo.getAllDiets()
                _uiState.value = _uiState.value.copy(items = list, isLoading = false)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }

    fun addItem(diet: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updatedList = repo.addDiet(token, DietsIDRequest(diet))
                _uiState.value = _uiState.value.copy(items = updatedList, isLoading = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }

    fun removeItem(diet: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updatedList = repo.removeDiet(token, DietsIDRequest(diet))
                _uiState.value = _uiState.value.copy(items = updatedList, isLoading = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }
}
