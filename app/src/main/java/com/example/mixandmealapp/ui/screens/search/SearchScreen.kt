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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.enums.KitchenStyle
import com.example.mixandmealapp.models.enums.MealType
import com.example.mixandmealapp.models.requests.RecipeSearchRequest
import com.example.mixandmealapp.ui.components.PopularRecipeCard
import com.example.mixandmealapp.ui.screens.upload.FilterSection
import com.example.mixandmealapp.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Locale
import java.util.Locale.getDefault


@Composable
fun SearchScreen(
    onSearch: (RecipeSearchRequest) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var maxCookingTime: Int? by remember { mutableStateOf(null) }

    // Filter states
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedKitchenStyle by remember { mutableStateOf<String?>(null) }
    var selectedMealType by remember { mutableStateOf<String?>(null) }

    // Allergen states - multi-select
    var selectedAllergens by remember {
        mutableStateOf<Set<String>>(emptySet())
    }

    // Diet states - multi-select
    var selectedDiets by remember {
        mutableStateOf<Set<String>>(emptySet())
    }

    // Allergen list
    val allergenList = remember {
        listOf(
            AllergenEntry(1, "gluten", "Gluten", ""),
            AllergenEntry(2, "crustaceans", "Crustaceans", ""),
            AllergenEntry(3, "eggs", "Eggs", ""),
            AllergenEntry(4, "fish", "Fish", ""),
            AllergenEntry(5, "peanuts", "Peanuts", ""),
            AllergenEntry(6, "soy", "Soy", ""),
            AllergenEntry(7, "milk", "Milk", ""),
            AllergenEntry(8, "treenuts", "Tree Nuts", ""),
            AllergenEntry(9, "celery", "Celery", ""),
            AllergenEntry(10, "mustard", "Mustard", ""),
            AllergenEntry(11, "sesame", "Sesame", ""),
            AllergenEntry(12, "sulphites", "Sulphites", ""),
            AllergenEntry(13, "lupin", "Lupin", ""),
            AllergenEntry(14, "molluscs", "Molluscs", ""),
            AllergenEntry(15, "corn", "Corn", "")
        )
    }
    // Diet list
    val dietList = remember {
        listOf(
            DietEntry(1, "Veganistisch", ""),
            DietEntry(2, "Vegetarisch", ""),
            DietEntry(3, "Glutenvrij", ""),
            DietEntry(4, "Lactosevrij", ""),
            DietEntry(5, "Notenvrij", ""),
            DietEntry(6, "Zuivelvrij", ""),
            DietEntry(7, "Suikerarm", ""),
            DietEntry(8, "Zoutarm", ""),
            DietEntry(9, "Halal", ""),
            DietEntry(10, "Kosher", ""),
            DietEntry(11, "Paleo", ""),
            DietEntry(12, "Keto", ""),
            DietEntry(13, "Raw food", ""),
            DietEntry(14, "Flexitarisch", "")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // First row: search field + search button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                        val difficultyToSend = selectedDifficulty?.uppercase()

                        onSearch(
                            RecipeSearchRequest(
                                searchQuery,
                                difficultyToSend,
                                null,
                                null,
                                null,
                                emptyList(),
                                emptyList(),
                                emptyList(),
                            )
                        )
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Search")
            }
        }

        // Second row: max cooking time
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = maxCookingTime?.toString() ?: "",
                onValueChange = {
                    maxCookingTime = it.toIntOrNull()
                },
                label = { Text("Max cooking time (min)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        // Difficulty Filter Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Difficulty",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(Difficulty.entries.toTypedArray()) { difficulty ->
                    FilterChip(
                        selected = selectedDifficulty == difficulty.difficultyName,
                        onClick = { selectedDifficulty = difficulty.difficultyName },
                        label = { Text(difficulty.difficultyName.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }

//        // Kitchen Style Filter Buttons
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//        ) {
//            Text(
//                text = "Kitchen Style",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                contentPadding = PaddingValues(horizontal = 4.dp)
//            ) {
//                items(KitchenStyle.entries.toTypedArray()) { style ->
//                    FilterChip(
//                        selected = selectedKitchenStyle == style,
//                        onClick = { },
//                        label = { Text(style.kitchenName.replaceFirstChar { it.uppercase() }) },
//                        modifier = Modifier
//                    )
//                }
//            }
//        }
//
//        // Meal Type Filter Buttons
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//        ) {
//            Text(
//                text = "Meal Type",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                contentPadding = PaddingValues(horizontal = 4.dp)
//            ) {
//                items(MealType.entries.toTypedArray()) { mealType ->
//                    FilterChip(
//                        selected = selectedMealType == mealType,
//                        onClick = {  },
//                        label = { Text(mealType.mealTypeName.replaceFirstChar { it.uppercase() }) },
//                        modifier = Modifier
//                    )
//                }
//            }
//        }

        // Allergen Filter Buttons (multi-select)
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//        ) {
//            Text(
//                text = "Exclude Allergens (${selectedAllergens.size})",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(6.dp),
//                contentPadding = PaddingValues(horizontal = 4.dp)
//            ) {
//                items(allergenList) { allergen ->
//                    FilterChip(
//                        selected = allergen.name in selectedAllergens,
//                        onClick = { },
//                        label = {
//                            Text(
//                                text = allergen.displayName,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
//                        }
//                    )
//                }
//            }
//        }
// Diet Filter Buttons (multi-select)
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//        ) {
//            Text(
//                text = "Diets (${selectedDiets.size})",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            LazyRow(
//                horizontalArrangement = Arrangement.spacedBy(6.dp),
//                contentPadding = PaddingValues(horizontal = 4.dp)
//            ) {
//                items(dietList) { diet ->
//                    FilterChip(
//                        selected = diet.displayName in selectedDiets,
//                        onClick = {  },
//                        label = {
//                            Text(
//                                text = diet.displayName,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
//                        }
//                    )
//                }
//            }
//        }
    }
}





//    FilterSection(
//        title = "Kitchen Styles",
//        options = FilterOptions.kitchenStyles,
//        selectedFilters = selectedFilters,
//        onFilterToggle = { resId -> viewModel.toggleFilter(resId) }
//    )



//
//        // Filter chips sections

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