package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.models.entries.UserFridgeEntry
import com.example.mixandmealapp.models.requests.IngredientIDRequest
import com.example.mixandmealapp.repository.FridgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FridgeUiState(
    val items: List<UserFridgeEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FridgeViewModel(
    private val repo: FridgeRepository = FridgeRepository(),
    private val tokenRepo: TokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FridgeUiState())
    val uiState: StateFlow<FridgeUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val list = repo.getFridgeItems(token)
                _uiState.value = _uiState.value.copy(items = list, isLoading = false)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }

    fun addItem(ingredient: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updatedList = repo.addIngredientToFridge(token, IngredientIDRequest(ingredient))
                _uiState.value = _uiState.value.copy(items = updatedList, isLoading = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }

    fun removeItem(id: UserFridgeEntry) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updatedList = repo.removeIngredientFromFridge(token, IngredientIDRequest(id.ingredientName))
                _uiState.value = _uiState.value.copy(items = updatedList, isLoading = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }
}
