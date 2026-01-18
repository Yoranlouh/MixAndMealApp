package com.example.mixandmealapp.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mixandmealapp.ui.components.PopularRecipeCard
import com.example.mixandmealapp.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchScreen(
    onSearch: (String, Int?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavController,
//    onSpeechRecognize: (callback: (String?) -> Unit) -> Unit

) {
    var scope = rememberCoroutineScope()
//    val recipes by viewModel.filteredRecipes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
//    val searchQuery by viewModel.searchQuery.collectAsState()
    var maxCookingTime: Int? by remember { mutableStateOf(0) }

//    val selectedFilters by viewModel.selectedFilters.collectAsState()
//    var isLoading by viewModel.isLoading.collectAsState()
//
    Column(modifier = modifier.fillMaxSize()) {
        // Search and Max Time inputs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search recipes") },
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
            Button(
                onClick = {
                    scope.launch {
                        onSearch(
                            searchQuery,
                            maxCookingTime,

                            )
                    }
                }
            ) {
                Text("Search")
            }
        }
            OutlinedTextField(
                value = maxCookingTime.toString(),
                onValueChange = { maxCookingTime = it.toIntOrNull() },
                label = { Text("Max time (min)") },
                modifier = Modifier.width(120.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
//
//        // Filter chips sections
//        FilterSection(
//            title = "Kitchen Styles",
//            options = FilterOptions.kitchenStyles,
//            selectedFilters = selectedFilters,
//            onFilterToggle = { resId -> viewModel.toggleFilter(resId) }
//        )
//        FilterSection(
//            title = "Meal Types",
//            options = FilterOptions.mealTypes,
//            selectedFilters = selectedFilters,
//            onFilterToggle = { resId -> viewModel.toggleFilter(resId) }
//        )
//        FilterSection(
//            title = "Allergens",
//            options = FilterOptions.allergens,
//            selectedFilters = selectedFilters,
//            onFilterToggle = { resId -> viewModel.toggleFilter(resId) }
//        )
//        FilterSection(
//            title = "Diets",
//            options = FilterOptions.diets,
//            selectedFilters = selectedFilters,
//            onFilterToggle = { resId -> viewModel.toggleFilter(resId) }
//        )
//
//        // Clear filters button
//        TextButton(
//            onClick = { viewModel.clearAllFilters() },
//            modifier = Modifier.padding(horizontal = 16.dp)
//        ) {
//            Text("Clear all filters")
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            TextButton(
//                onClick = { viewModel.clearAllFilters() },
//                modifier = Modifier.weight(1f)
//            ) {
//                Text("Clear all filters")
//            }
//
//            Button(
//                onClick = {
//                    isLoading = true
//                    viewModel.loadFilteredRecipes()
//                    // Reset loading state after a short delay (or handle via ViewModel callback)
//                    coroutineScope.launch(Unit) {
//                        kotlinx.coroutines.delay(1000)
//                        isLoading = false
//                    }
//                },
//                modifier = Modifier.weight(1f),
//                enabled = !isLoading
//            ) {
//                if (isLoading) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text("Loading...")
//                    }
//                } else {
//                    Text("Load Filtered Recipes")
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Recipes grid
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            contentPadding = PaddingValues(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(recipes) { recipe ->
//                PopularRecipeCard(
//                    recipe = recipe,
//                    onClick = { /* Navigate to recipe details */ }
//                )
//            }
//
//            if (recipes.isEmpty()) {
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .fillMaxHeight(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "No recipes found",
//                            style = MaterialTheme.typography.bodyLarge
//                        )
//                    }
//                }
//            }
//        }
//    }
    }


//
//@Composable
//private fun FilterSection(
//    title: String,
//    options: List<Int>,
//    selectedFilters: Set<Int>,
//    onFilterToggle: (Int) -> Unit
//) {
//    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//        Text(
//            text = title,
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//        LazyRow(
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(options) { resId ->
//                val text = stringResource(resId)
//                FilterChip(
//                    selected = selectedFilters.contains(resId),
//                    onClick = { onFilterToggle(resId) },
//                    label = { Text(text) },
//                    modifier = Modifier
//                )
//            }
//        }
//        Spacer(modifier = Modifier.height(12.dp))
//    }
//}