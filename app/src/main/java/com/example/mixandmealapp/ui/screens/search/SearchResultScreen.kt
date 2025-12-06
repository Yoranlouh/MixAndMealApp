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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange

@Composable
fun SearchResultScreen(navController: NavHostController) {
    val handle = navController.previousBackStackEntry?.savedStateHandle
    var query by remember { mutableStateOf(handle?.get<String>("query") ?: "") }
    var selKitchens by remember { mutableStateOf((handle?.get<List<String>>("kitchenStyles") ?: emptyList()).toSet()) }
    var selMeals by remember { mutableStateOf((handle?.get<List<String>>("mealTypes") ?: emptyList()).toSet()) }
    var selAllergens by remember { mutableStateOf((handle?.get<List<String>>("allergens") ?: emptyList()).toSet()) }
    var selDiets by remember { mutableStateOf((handle?.get<List<String>>("diets") ?: emptyList()).toSet()) }
    var showFilters by remember { mutableStateOf(false) }

    val results = remember(query, selKitchens, selMeals, selAllergens, selDiets) {
        MockRecipeRepository.search(
            SearchArgs(
                query = query,
                kitchenStyles = selKitchens,
                mealTypes = selMeals,
                allergens = selAllergens,
                diets = selDiets
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        // Top search field with filter icon
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            singleLine = true,
            placeholder = { Text("Search…") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = BrandGrey) },
            trailingIcon = {
                val activeCount = selKitchens.size + selMeals.size + selAllergens.size + selDiets.size
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

        // Compact filter summary
        CompactFilterSummary(
            selectedKitchenStyles = selKitchens,
            selectedMealTypes = selMeals,
            selectedAllergens = selAllergens,
            selectedDiets = selDiets,
            onOpenFilters = { showFilters = true },
            onClearAll = { selKitchens = emptySet(); selMeals = emptySet(); selAllergens = emptySet(); selDiets = emptySet() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Found ${results.size} result(s)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(results, key = { it.id }) { r ->
                SearchResultItem(recipe = r)
            }
        }
    }

    // Bottom sheet to adjust filters
    SearchFilterBottomSheet(
        show = showFilters,
        selectedKitchenStyles = selKitchens,
        selectedMealTypes = selMeals,
        selectedAllergens = selAllergens,
        selectedDiets = selDiets,
        onToggleKitchen = { selKitchens = selKitchens.toggle(it) },
        onToggleMealType = { selMeals = selMeals.toggle(it) },
        onToggleAllergen = { selAllergens = selAllergens.toggle(it) },
        onToggleDiet = { selDiets = selDiets.toggle(it) },
        onApply = { showFilters = false },
        onClearAll = { selKitchens = emptySet(); selMeals = emptySet(); selAllergens = emptySet(); selDiets = emptySet() },
        onDismiss = { showFilters = false }
    )
}

private fun <T> Set<T>.toggle(item: T): Set<T> = if (contains(item)) this - item else this + item