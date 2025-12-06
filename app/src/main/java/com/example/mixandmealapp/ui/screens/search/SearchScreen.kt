package com.example.mixandmealapp.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mixandmealapp.ui.components.BottomNavBar
import com.example.mixandmealapp.ui.components.truncate
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.DarkText
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import com.example.mixandmealapp.ui.screens.search.CompactFilterSummary

@Composable
fun SearchScreen(navController: NavHostController) {
    Scaffold { paddingValues ->
        MinimalSearchContent(
            modifier = Modifier.padding(paddingValues),
            onSearch = { query, kitchens, meals, allergens, diets ->
                navController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("query", query)
                    set("kitchenStyles", kitchens.toList())
                    set("mealTypes", meals.toList())
                    set("allergens", allergens.toList())
                    set("diets", diets.toList())
                }
                navController.navigate(Navigation.SEARCH_RESULTS)
            }
        )
    }
}

@Composable
fun MinimalSearchContent(
    modifier: Modifier = Modifier,
    onSearch: (
        query: String,
        kitchenStyles: Set<String>,
        mealTypes: Set<String>,
        allergens: Set<String>,
        diets: Set<String>
    ) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    var selKitchens by remember { mutableStateOf(setOf<String>()) }
    var selMeals by remember { mutableStateOf(setOf<String>()) }
    var selAllergens by remember { mutableStateOf(setOf<String>()) }
    var selDiets by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "What are we making today?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Modern search bar with leading search icon and trailing filter icon with badge when active
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(56.dp),
            placeholder = { Text(text = stringResource(id = com.example.mixandmealapp.R.string.search)) },
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(id = com.example.mixandmealapp.R.string.search), tint = BrandGrey)
            },
            trailingIcon = {
                val activeCount = selKitchens.size + selMeals.size + selAllergens.size + selDiets.size
                Box {
                    IconButton(onClick = { showFilters = true }) {
                        Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Filters")
                    }
                    if (activeCount > 0) {
                        // tiny orange dot to indicate active filters
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .align(Alignment.TopEnd)
                                .background(BrandOrange, shape = RoundedCornerShape(50))
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandOrange,
                unfocusedBorderColor = BrandGrey,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        )

        // Suggested quick labels
        Spacer(modifier = Modifier.height(10.dp))
        val suggested = listOf("Breakfast", "Lunch", "Dinner", "Dessert")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(suggested) { label ->
                FilterChip(
                    selected = selMeals.contains(label),
                    onClick = {
                        selMeals = setOf(label)
                        // navigate immediately to results for this meal type
                        onSearch(searchQuery, selKitchens, selMeals, selAllergens, selDiets)
                    },
                    label = { Text(label) }
                )
            }
        }

        // Compact filter dropdown summary
        CompactFilterSummary(
            selectedKitchenStyles = selKitchens,
            selectedMealTypes = selMeals,
            selectedAllergens = selAllergens,
            selectedDiets = selDiets,
            onOpenFilters = { showFilters = true },
            onClearAll = { selKitchens = emptySet(); selMeals = emptySet(); selAllergens = emptySet(); selDiets = emptySet() }
        )

        Button(onClick = { onSearch(searchQuery, selKitchens, selMeals, selAllergens, selDiets) }) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Search")
        }
    }

    // Filters bottom sheet
    SearchFilterBottomSheet(
        show = showFilters,
        selectedKitchenStyles = selKitchens,
        selectedMealTypes = selMeals,
        selectedAllergens = selAllergens,
        selectedDiets = selDiets,
        onToggleKitchen = { opt -> selKitchens = selKitchens.toggle(opt) },
        onToggleMealType = { opt -> selMeals = selMeals.toggle(opt) },
        onToggleAllergen = { opt -> selAllergens = selAllergens.toggle(opt) },
        onToggleDiet = { opt -> selDiets = selDiets.toggle(opt) },
        onApply = { showFilters = false },
        onClearAll = {
            selKitchens = emptySet(); selMeals = emptySet(); selAllergens = emptySet(); selDiets = emptySet()
        },
        onDismiss = { showFilters = false }
    )
}

private fun <T> Set<T>.toggle(item: T): Set<T> = if (contains(item)) this - item else this + item

@Composable
private fun ActiveFilterChips(
    kitchens: Set<String>,
    meals: Set<String>,
    allergens: Set<String>,
    diets: Set<String>,
    onRemove: (category: String, value: String) -> Unit,
    onClearAll: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(0.95f)) {
        // Clear all action
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Active filters", style = MaterialTheme.typography.labelMedium, color = BrandGrey)
            TextButton(onClick = onClearAll) { Text("Clear all") }
        }

        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(kitchens.toList()) { k -> FilterChipPill(text = k) { onRemove("kitchen", k) } }
            items(meals.toList()) { m -> FilterChipPill(text = m) { onRemove("meal", m) } }
            items(allergens.toList()) { a -> FilterChipPill(text = a) { onRemove("allergen", a) } }
            items(diets.toList()) { d -> FilterChipPill(text = d) { onRemove("diet", d) } }
        }
    }
}

@Composable
private fun FilterChipPill(text: String, onRemove: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = BrandGrey,
        contentColor = DarkText
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
            Text(text)
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(onClick = onRemove, modifier = Modifier.size(18.dp)) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Remove filter", tint = DarkText)
            }
        }
    }
}

@Composable
fun SearchBarComponent(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        placeholder = { Text(stringResource(id = com.example.mixandmealapp.R.string.search_placeholder), color = BrandGrey) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = com.example.mixandmealapp.R.string.search),
                tint = BrandGrey
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = BrandGrey,
            unfocusedContainerColor = BrandGrey,
            disabledContainerColor = BrandGrey,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

@Composable
fun CategoryFilterSection(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        stringResource(id = com.example.mixandmealapp.R.string.breakfast),
        stringResource(id = com.example.mixandmealapp.R.string.lunch),
        stringResource(id = com.example.mixandmealapp.R.string.dinner)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selectedCategory
            Button(
                onClick = { onCategorySelected(category) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) BrandOrange else BrandGrey,
                    contentColor = if (isSelected) Color.White else DarkText
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(category)
            }
        }
    }
}

@Composable
fun PopularRecipesSection(onViewAll: () -> Unit = {}, onRecipeClick: () -> Unit = {}) {
    val recipes = listOf(
        "Egg & Avocado".truncate(10),
        "Bowl of rice".truncate(10),
        "Chicken Soup".truncate(10)
    )


    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.popular_recipes),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.view_all),
                style = MaterialTheme.typography.bodyMedium,
                color = BrandOrange,
                modifier = Modifier.clickable { onViewAll() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recipes) { recipe ->
                PopularRecipeCard(recipe, onClick = onRecipeClick)
            }
        }
    }
}

@Composable
fun PopularRecipeCard(recipeName: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BrandGrey, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = recipeName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun EditorsChoiceSection(onViewAll: () -> Unit = {}, onRecipeClick: () -> Unit = {}) {
    val recipes = listOf(
        EditorRecipe("Easy homemade beef burger"),
        EditorRecipe("Blueberry with egg for breakfast")
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.editors_choice),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.view_all),
                style = MaterialTheme.typography.bodyMedium,
                color = BrandOrange,
                modifier = Modifier.clickable { onViewAll() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        recipes.forEach { recipe ->
            EditorChoiceCard(recipe = recipe, onClick = onRecipeClick)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

data class EditorRecipe(val title: String)

@Composable
fun EditorChoiceCard(recipe: EditorRecipe, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Recipe Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(BrandGrey, RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Recipe Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                // Removed author avatar placeholder as per design
            }

            // Small arrow in orange rounded square
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(36.dp)
                    .background(BrandOrange, shape = RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = stringResource(id = com.example.mixandmealapp.R.string.go_to_details),
                    tint = Color.White
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MixAndMealAppTheme {
        androidx.navigation.compose.rememberNavController().let { navController ->
            SearchScreen(navController)
        }
    }
}
