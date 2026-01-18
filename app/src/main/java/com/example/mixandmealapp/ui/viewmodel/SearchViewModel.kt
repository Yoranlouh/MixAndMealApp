// In app/src/main/java/com/example/mixandmealapp/ui/viewmodel/SearchViewModel.kt
package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.models.requests.RecipeSearchRequest
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.network.ApiService
import com.example.mixandmealapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class SearchUiState(
    val recipes: List<RecipeCardResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    var uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    fun searchRecipes(search: RecipeSearchRequest) {
        viewModelScope.launch {
            try {
                    val recipeCardsDeferred = async { repository.searchRecipeRequest(search) }
                    val recipeCards = recipeCardsDeferred.await()
                    _uiState.value = _uiState.value.copy(
                        recipes = recipeCards,
                        isLoading = false
                    )
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = "Error fetching recipes")
            }
        }
    }
}




    // Add these to your SearchViewModel class
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _filteredRecipes = MutableStateFlow<List<RecipeCardResponse>>(emptyList())
//    val filteredRecipes: StateFlow<List<RecipeCardResponse>> = _filteredRecipes.asStateFlow()
//
//    private val _allRecipes = MutableStateFlow<List<RecipeCardResponse>>(emptyList())
//
//    val searchQuery = MutableStateFlow("")
//    val maxCookingTime = MutableStateFlow(0)
//    val selectedFilters = MutableStateFlow<String?>(null)
//
////    init {
////        combine(
////            _allRecipes,
////            searchQuery,
////            maxCookingTime,
////            selectedFilters
////        ) { recipes, query, maxTime, filters ->
////            filterRecipes(recipes, query, maxTime, filters)
////        }.onEach { filteredRecipes = it }.launchIn(viewModelScope)
////    }
//
//    private fun loadRecipes() {
//        viewModelScope.launch {
//            _allRecipes.value = repository.getAllRecipes()
//        }
//    }
//
//    fun updateSearchQuery(query: String) {
//        searchQuery.value = query
//    }
//
//    fun updateMaxCookingTime(time: Int) {
//        maxCookingTime.value = time
//    }
//
//    fun toggleFilter(filterId: Int) {
//        val current = selectedFilters.value
//        selectedFilters.value = current
//    }
//
//    fun clearAllFilters() {
//        selectedFilters.value = toString()
//        searchQuery.value = ""
//        maxCookingTime.value = 0
//    }
//
//    fun loadFilteredRecipes() {
//        viewModelScope.launch {
//            try {
//                isLoading.value = true
//
//                // Build the search request from current filter states
//                val searchRequest = RecipeSearchRequest(
//                    partialTitle = if (searchQuery.value.isNotBlank()) searchQuery.value else null,
//                    difficulty = null, // Not implemented in UI yet
//                    mealType = getSelectedMealType(selectedFilters.value),
//                    kitchenStyle = getSelectedKitchenStyle(selectedFilters.value),
//                    maxCookingTime = if (maxCookingTime.value > 0) maxCookingTime.value else null,
//                    diets = getSelectedFiltersByCategory(selectedFilters.value, FilterOptions.diets),
//                    allergens = getSelectedFiltersByCategory(selectedFilters.value, FilterOptions.allergens),
//                    ingredients = emptyList() // Not implemented in UI yet
//                )
//
//                // Call repository with the search request
//                val filteredRecipes = repository.searchRecipes(searchRequest)
//                this@SearchViewModel.filteredRecipes.value = filteredRecipes
//
//            } catch (e: Exception) {
//                // Handle error
//                println("Search failed: ${e.message}")
//            } finally {
//                isLoading.value = false
//            }
//        }
//    }
//
//    @Composable
//    private fun getSelectedMealType(selectedFilters: Set<Int>): String? {
//        val selectedMealTypeIds = selectedFilters.intersect(FilterOptions.mealTypes.toSet())
//        return if (selectedMealTypeIds.isNotEmpty()) {
//            selectedMealTypeIds.first().let { stringResource(it) }
//        } else null
//    }
//
//    @Composable
//    private fun getSelectedKitchenStyle(selectedFilters: Set<Int>): String? {
//        val selectedKitchenStyleIds = selectedFilters.intersect(FilterOptions.kitchenStyles.toSet())
//        return if (selectedKitchenStyleIds.isNotEmpty()) {
//            selectedKitchenStyleIds.first().let { stringResource(it) }
//        } else null
//    }
//
//    private fun getSelectedFiltersByCategory(
//        selectedFilters: Set<Int>,
//        categoryOptions: List<Int>
//    ): List<Int> {
//        return selectedFilters.intersect(categoryOptions.toSet()).toList()
//    }
//}
