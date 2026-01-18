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
import org.koin.core.KoinApplication.Companion.init
import java.util.Locale
import java.util.Locale.getDefault


@Composable
fun SearchScreen(
    onSearch: (RecipeSearchRequest) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.reloadSearchScreen()
    }

    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var maxCookingTime: Int? by remember { mutableStateOf(null) }

    // Filter states
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedKitchenStyle by remember { mutableStateOf<String?>(null) }
    var selectedMealType by remember { mutableStateOf<String?>(null) }

    // Allergen states - multi-select
    var selectedAllergens by remember { mutableStateOf<List<Int>>(emptyList()) }

    // Diet states - multi-select
    var selectedDiets by remember { mutableStateOf<List<Int>>(emptyList()) }

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
                                selectedMealType,
                                selectedKitchenStyle,
                                maxCookingTime,
                                selectedDiets,
                                selectedAllergens,
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

        // Kitchen Style Filter Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Kitchen Style",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(KitchenStyle.entries.toTypedArray()) { style ->
                    FilterChip(
                        selected = selectedKitchenStyle == style.kitchenName,
                        onClick = { selectedKitchenStyle = style.kitchenName },
                        label = { Text(style.kitchenName.replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier
                    )
                }
            }
        }

        // Meal Type Filter Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Meal Type",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(MealType.entries.toTypedArray()) { mealType ->
                    FilterChip(
                        selected = selectedMealType == mealType.mealTypeName,
                        onClick = { selectedMealType = mealType.mealTypeName },
                        label = { Text(mealType.mealTypeName.replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier
                    )
                }
            }
        }

//         Allergen Filter Buttons (multi-select)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Exclude Allergens (${selectedAllergens.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(allergenList) { allergen ->
                    FilterChip(
                        selected = selectedAllergens == allergen,
                        onClick = { selectedAllergens },
                        label = {
                            Text(
                                text = allergen.displayName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }
// Diet Filter Buttons (multi-select)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Diets (${selectedDiets.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(dietList) { diet ->
                    FilterChip(
                        selected = selectedDiets == diet,
                        onClick = { selectedDiets },
                        label = {
                            Text(
                                text = diet.displayName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }
    }
}

