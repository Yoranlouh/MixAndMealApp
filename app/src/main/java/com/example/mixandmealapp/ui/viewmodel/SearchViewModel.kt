// In app/src/main/java/com/example/mixandmealapp/ui/viewmodel/SearchViewModel.kt
package com.example.mixandmealapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.network.ApiService
import com.example.mixandmealapp.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val recipeRepository: RecipeRepository = RecipeRepository()) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<RecipeCardResponse>>(emptyList())
    val searchResults: StateFlow<List<RecipeCardResponse>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun searchRecipes(
        query: String,
        kitchens: Set<String>,
        meals: Set<String>,
        allergens: Set<String>,
        diets: Set<String>
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = recipeRepository.searchRecipes(query, kitchens, meals, allergens, diets)
                _searchResults.value = results
            } catch (e: Exception) {
                // Handle exceptions, e.g., log the error or show a message to the user
                _searchResults.value = emptyList() // Clear results on error
            } finally {
                _isLoading.value = false
            }
        }
    }
}