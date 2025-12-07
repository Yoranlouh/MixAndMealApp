package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.RecipesRepository
import com.example.mixandmealapp.data.ServiceLocator
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

data class HomeUiState(
    val featured: List<String> = emptyList(),
    val popular: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val repo: RecipesRepository = ServiceLocator.recipesRepository
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    fun refresh() {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val featuredDeferred = async { repo.getFeatured() }
                val popularDeferred = async { repo.getPopular(page = 1) }
                val featured = featuredDeferred.await()
                val popular = popularDeferred.await()
                uiState = uiState.copy(featured = featured, popular = popular, isLoading = false)
            } catch (t: Throwable) {
                uiState = uiState.copy(isLoading = false, error = t.message)
            }
        }
    }
}
