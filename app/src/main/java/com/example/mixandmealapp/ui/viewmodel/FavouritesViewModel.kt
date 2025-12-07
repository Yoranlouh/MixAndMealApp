package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.FavouritesRepository
import com.example.mixandmealapp.data.ServiceLocator
import kotlinx.coroutines.launch

data class FavouritesUiState(
    val favourites: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavouritesViewModel(
    private val repository: FavouritesRepository = ServiceLocator.favouritesRepository
) : ViewModel() {
    var uiState by mutableStateOf(FavouritesUiState())
        private set

    fun load() {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val list = repository.getFavourites()
                uiState = uiState.copy(favourites = list, isLoading = false)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message, isLoading = false)
            }
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            try {
                repository.removeFavourite(id)
                uiState = uiState.copy(favourites = uiState.favourites.filterNot { it == id })
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }
}
