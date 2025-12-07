package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.FridgeItem
import com.example.mixandmealapp.data.FridgeRepository
import com.example.mixandmealapp.data.ServiceLocator
import kotlinx.coroutines.launch

data class FridgeUiState(
    val items: List<FridgeItem> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FridgeViewModel(
    private val repo: FridgeRepository = ServiceLocator.fridgeRepository
) : ViewModel() {
    var uiState by mutableStateOf(FridgeUiState())
        private set

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            try {
                val list = repo.getItems()
                uiState = uiState.copy(items = list)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }

    fun addItem(name: String, quantity: String) {
        viewModelScope.launch {
            try {
                val added = repo.addItem(name, quantity)
                val next = uiState.items + added
                val suggestions = repo.getSuggestions(next)
                uiState = uiState.copy(items = next, suggestions = suggestions)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }

    fun removeItem(id: String) {
        viewModelScope.launch {
            try {
                repo.removeItem(id)
                val next = uiState.items.filterNot { it.id == id }
                val suggestions = repo.getSuggestions(next)
                uiState = uiState.copy(items = next, suggestions = suggestions)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }

    fun loadSuggestions() {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val suggestions = repo.getSuggestions(uiState.items)
                uiState = uiState.copy(suggestions = suggestions, isLoading = false)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message, isLoading = false)
            }
        }
    }
}
