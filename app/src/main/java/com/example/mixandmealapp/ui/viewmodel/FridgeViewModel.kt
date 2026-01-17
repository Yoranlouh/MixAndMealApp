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
import kotlinx.coroutines.launch

data class FridgeUiState(
    val items: List<UserFridgeEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FridgeViewModel(
    private val repo: FridgeRepository = FridgeRepository(),
    private val tokenRepo : TokenRepository
) : ViewModel() {
    var uiState by mutableStateOf(FridgeUiState())
        private set

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            try {
                val tokenToUse = tokenRepo.getTokenOrDefault()
                val list = repo.getFridgeItems(tokenToUse)
                uiState = uiState.copy(items = list)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }

    fun addItem(ingredient : String) {
        viewModelScope.launch {
            try {
                val tokenToUse = tokenRepo.getTokenOrDefault()
                val added = repo.addIngredientToFridge(tokenToUse, IngredientIDRequest(ingredient) )
                val next = uiState.items + added
                uiState = uiState.copy(items = next)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }

    fun removeItem(id: UserFridgeEntry) {
        viewModelScope.launch {
            try {
                val tokenToUse = tokenRepo.getTokenOrDefault()
                repo.removeIngredientFromFridge(tokenToUse, IngredientIDRequest(id.ingredientName))
                val next = uiState.items.filterNot { it == id }
                uiState = uiState.copy(items = next)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }
}
