package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.FavouritesRepository
import com.example.mixandmealapp.data.RecipesRepository
import com.example.mixandmealapp.data.ServiceLocator
import kotlinx.coroutines.launch

data class RecipeDetailUiState(
    val recipeId: String? = null,
    val isFavorite: Boolean = false,
    val servings: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipeDetailViewModel(
    private val recipes: RecipesRepository = ServiceLocator.recipesRepository,
    private val favourites: FavouritesRepository = ServiceLocator.favouritesRepository
) : ViewModel() {
    var uiState by mutableStateOf(RecipeDetailUiState())
        private set

    fun load(id: String) {
        uiState = uiState.copy(isLoading = true, recipeId = id, error = null)
        viewModelScope.launch {
            try {
                // For now, we only receive a String title as the recipe data
                val title = recipes.getById(id)
                // Consider favorite if currently in favourites list
                val favs = favourites.getFavourites()
                uiState = uiState.copy(
                    isLoading = false,
                    recipeId = title,
                    isFavorite = favs.contains(title)
                )
            } catch (t: Throwable) {
                uiState = uiState.copy(isLoading = false, error = t.message)
            }
        }
    }

    fun toggleFavorite() {
        val id = uiState.recipeId ?: return
        val target = !uiState.isFavorite
        uiState = uiState.copy(isFavorite = target)
        viewModelScope.launch {
            try {
                if (target) favourites.addFavourite(id) else favourites.removeFavourite(id)
            } catch (t: Throwable) {
                // revert on failure
                uiState = uiState.copy(isFavorite = !target, error = t.message)
            }
        }
    }

    fun changeServings(delta: Int) {
        val new = (uiState.servings + delta).coerceAtLeast(1)
        uiState = uiState.copy(servings = new)
    }
}
