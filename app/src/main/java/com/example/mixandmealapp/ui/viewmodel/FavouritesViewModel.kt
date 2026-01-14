package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.models.requests.RecipeIDRequest
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.repository.UserRepository
import kotlinx.coroutines.launch

data class FavouritesUiState(
    val favourites: List<RecipeCardResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavouritesViewModel(
    private val repo: TokenRepository,
    private val userRepo: UserRepository
) : ViewModel() {
    var uiState by mutableStateOf(FavouritesUiState())
        private set

    fun load() {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val tokenToUse = repo.getTokenOrDefault()
                val list = userRepo.getFavourites(tokenToUse)
                uiState = uiState.copy(favourites = list, isLoading = false)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message, isLoading = false)
            }
        }
    }

    fun toggleFavourite(id: RecipeIDRequest) {
        viewModelScope.launch {
            try {
                val tokenToUse = repo.getTokenOrDefault()
                val favourites = userRepo.toggleFavourite(tokenToUse, id)
                uiState = uiState.copy(favourites, isLoading = false)
                load()
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }
}
