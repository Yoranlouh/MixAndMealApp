// In app/src/main/java/com/example/mixandmealapp/ui/viewmodel/SearchViewModel.kt
package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.models.requests.RecipeSearchRequest
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.network.ApiService
import com.example.mixandmealapp.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val recipes: List<RecipeCardResponse> = emptyList(),
    val searchRequest : RecipeSearchRequest = RecipeSearchRequest("","","","",100,listOf(),listOf(),listOf()),
    val isLoading: Boolean = false,
    val error: String? = null
)
class SearchViewModel(private val recipeRepository: RecipeRepository = RecipeRepository()) : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    fun load(request: RecipeSearchRequest) {
        uiState = uiState.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val list = recipeRepository.searchRecipeRequest(request)
                uiState = uiState.copy(recipes = list, searchRequest = request, isLoading = false)
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message, isLoading = false)
            }
        }
    }
}