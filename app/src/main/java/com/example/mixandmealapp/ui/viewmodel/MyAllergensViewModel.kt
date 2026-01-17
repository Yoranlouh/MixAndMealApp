package com.example.mixandmealapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.UserAllergenEntry
import com.example.mixandmealapp.models.requests.AllergenIDRequest
import com.example.mixandmealapp.models.requests.IngredientIDRequest
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.mixandmealapp.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AllergensUiState(
    val items: List<AllergenEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AllergensViewModel(
    private val repo: UserRepository = UserRepository(),
    private val tokenRepo: TokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AllergensUiState())
    val uiState: StateFlow<AllergensUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val list = repo.getAllAllergens()
                _uiState.value = _uiState.value.copy(items = list, isLoading = false)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }

    fun addItem(allergen: AllergenEntry) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updatedList = repo.addAllergen(token, AllergenIDRequest(allergen.displayName))
                _uiState.value = _uiState.value.copy(items = updatedList, isLoading = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }

    fun removeItem(allergen: AllergenEntry) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updatedList = repo.removeAllergen(token, AllergenIDRequest(allergen.displayName))
                _uiState.value = _uiState.value.copy(items = updatedList, isLoading = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isLoading = false)
            }
        }
    }
}

