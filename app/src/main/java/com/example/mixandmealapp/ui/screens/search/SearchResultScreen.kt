package com.example.mixandmealapp.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mixandmealapp.models.requests.RecipeSearchRequest
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.viewmodel.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchResultScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = koinViewModel(),
) {
    // VERWIJDER de searchViewModel.load(...) uit de LaunchedEffect die alles leegmaakt!
    // De data is al geladen door de actie in SearchScreen.

    val uiState = searchViewModel.uiState
    val items = uiState.recipes
    val currentRequest = uiState.searchRequest

    // Gebruik de waarden uit de ViewModel voor de UI
    var searchQuery by remember { mutableStateOf(currentRequest.partialTitle) }
    var showFilters by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                // Optioneel: direct zoeken bij typen
                searchViewModel.load(currentRequest.copy(partialTitle = it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            singleLine = true,
            placeholder = { Text("Search…") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = BrandGrey) },
            trailingIcon = {
                IconButton(onClick = { showFilters = true }) {
                    Icon(Icons.Filled.FilterList, contentDescription = "Filters")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandOrange,
                unfocusedBorderColor = BrandGrey,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        CompactFilterSummary(
            selectedKitchenStyles = currentRequest.kitchenStyle,
            selectedMealTypes = currentRequest.mealType,
            selectedAllergens = currentRequest.allergens,
            selectedDiets = currentRequest.diets,
            onOpenFilters = { showFilters = true },
            onClearAll = {
                searchViewModel.load(RecipeSearchRequest("", "EASY", "", "", 100, emptyList(), emptyList(), emptyList()))
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Found ${items.size} result(s)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp)
        )

        // The LazyColumn will now use the 'results' from the ViewModel
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // FIX: Ensure the variable name matches ('recipe' in this case)
            items(items, key = { it.recipeId }) { recipe ->
                // Now that it's imported, this call should be valid.
                SearchResultItem(navController = navController, recipe = recipe)
            }
        }
    }

    if (showFilters) {
        SearchFilterBottomSheet(
            show = showFilters,
            selectedKitchenStyles = currentRequest.kitchenStyle,
            selectedMealTypes = currentRequest.mealType,
            selectedAllergens = currentRequest.allergens,
            selectedDiets = currentRequest.diets,
            onToggleKitchen = { opt ->
                searchViewModel.load(currentRequest.copy(kitchenStyle = if(currentRequest.kitchenStyle == opt) "" else opt))
            },
            onToggleMealType = { opt ->
                searchViewModel.load(currentRequest.copy(mealType = if(currentRequest.mealType == opt) "" else opt))
            },
            onToggleAllergen = { opt ->
                val newList = if (currentRequest.allergens.contains(opt)) currentRequest.allergens - opt else currentRequest.allergens + opt
                searchViewModel.load(currentRequest.copy(allergens = newList))
            },
            onToggleDiet = {opt ->
                val newList = if (currentRequest.diets.contains(opt)) currentRequest.diets - opt else currentRequest.diets + opt
                searchViewModel.load(currentRequest.copy(allergens = newList))
            },
            onApply = { showFilters = false },
            onDismiss = { showFilters = false },
            onClearAll = { navController.navigate(Navigation.SEARCH) }
        )
    }}
