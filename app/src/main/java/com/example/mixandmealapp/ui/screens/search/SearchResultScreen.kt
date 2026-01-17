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
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.viewmodel.SearchViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchResultScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = koinViewModel(),
    query: String?,
    kitchens: String?,
    meals: String?,
    allergens: String?,
    diets: String?
) {
    // ✅ Use derivedStateOf to prevent recomposition issues
    var initialQuery by remember(query) { mutableStateOf(query ?: "") }
    var initialKitchens by remember(kitchens) { mutableStateOf(kitchens?.split(',')?.filter { it.isNotBlank() }?.toSet() ?: emptySet()) }
    var initialMeals by remember(meals) { mutableStateOf(meals?.split(',')?.filter { it.isNotBlank() }?.toSet() ?: emptySet()) }
    var initialAllergens by remember(allergens) { mutableStateOf(allergens?.split(',')?.filter { it.isNotBlank() }?.toSet() ?: emptySet()) }
    var initialDiets by remember(diets) { mutableStateOf(diets?.split(',')?.filter { it.isNotBlank() }?.toSet() ?: emptySet()) }

    // ✅ Separate mutable states that don't trigger ViewModel calls
    var searchQuery by remember { mutableStateOf(initialQuery) }
    var showFilters by remember { mutableStateOf(false) }

    val results by searchViewModel.searchResults.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()

    // ✅ Only trigger search when filters actually change
    LaunchedEffect(searchQuery, initialKitchens, initialMeals, initialAllergens, initialDiets) {
        searchViewModel.searchRecipes(searchQuery, initialKitchens, initialMeals, initialAllergens, initialDiets)
    }

    // ✅ Debounced search for query changes
    LaunchedEffect(searchQuery) {
        searchViewModel.searchRecipes(searchQuery, initialKitchens, initialMeals, initialAllergens, initialDiets)
    }


    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        // ... (Top search field is correct)
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
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
            selectedKitchenStyles = initialKitchens,
            selectedMealTypes = initialMeals,
            selectedAllergens = initialAllergens,
            selectedDiets = initialDiets,
            onOpenFilters = { showFilters = true },
            onClearAll = { initialKitchens = emptySet(); initialMeals = emptySet(); initialAllergens = emptySet(); initialDiets = emptySet() }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Found ${results.size} result(s)",
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
            items(results, key = { it.recipeId }) { recipe ->
                // Now that it's imported, this call should be valid.
                SearchResultItem(navController = navController, recipe = recipe)
            }
        }
    }

    // ... (SearchFilterBottomSheet is unchanged)
    SearchFilterBottomSheet(
        show = showFilters,
        selectedKitchenStyles = initialKitchens,
        selectedMealTypes = initialMeals,
        selectedAllergens = initialAllergens,
        selectedDiets = initialDiets,
        onToggleKitchen = { opt -> initialKitchens = if (initialKitchens.contains(opt)) emptySet() else setOf(opt) },
        onToggleMealType = { opt -> initialMeals = if (initialMeals.contains(opt)) emptySet() else setOf(opt) },
        onToggleAllergen = { initialAllergens = initialAllergens.toggle(it) },
        onToggleDiet = { opt -> initialDiets = if (initialDiets.contains(opt)) emptySet() else setOf(opt) },
        onApply = { showFilters = false },
        onClearAll = { initialKitchens = emptySet(); initialMeals = emptySet(); initialAllergens = emptySet(); initialDiets = emptySet() },
        onDismiss = { showFilters = false }
    )
}
private fun <T> Set<T>.toggle(item: T): Set<T> = if (contains(item)) this - item else this + item
