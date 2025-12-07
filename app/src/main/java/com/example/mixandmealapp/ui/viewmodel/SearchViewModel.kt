package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.RecipesRepository
import com.example.mixandmealapp.data.ServiceLocator
import com.example.mixandmealapp.common.toggle
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val kitchens: Set<String> = emptySet(),
    val meals: Set<String> = emptySet(),
    val allergens: Set<String> = emptySet(),
    val diets: Set<String> = emptySet(),
    val results: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel(
    private val repository: RecipesRepository = ServiceLocator.recipesRepository
) : ViewModel() {
    var uiState by mutableStateOf(SearchUiState())
        private set

    fun onQueryChange(q: String) { uiState = uiState.copy(query = q) }
    fun toggleKitchen(k: String) { uiState = uiState.copy(kitchens = uiState.kitchens.toggle(k)) }
    fun toggleMeal(m: String) { uiState = uiState.copy(meals = uiState.meals.toggle(m)) }
    fun toggleAllergen(a: String) { uiState = uiState.copy(allergens = uiState.allergens.toggle(a)) }
    fun toggleDiet(d: String) { uiState = uiState.copy(diets = uiState.diets.toggle(d)) }

    fun search() {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val results = repository.search(
                    query = uiState.query,
                    kitchens = uiState.kitchens,
                    meals = uiState.meals,
                    allergens = uiState.allergens,
                    diets = uiState.diets
                )
                uiState = uiState.copy(results = results, isLoading = false)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message, isLoading = false)
            }
        }
    }
}
