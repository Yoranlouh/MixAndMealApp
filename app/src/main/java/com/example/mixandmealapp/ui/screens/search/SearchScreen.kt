package com.example.mixandmealapp.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun SearchScreen(navController: NavHostController) {
    Scaffold { paddingValues ->
        SearchContent(
            modifier = Modifier.padding(paddingValues),
            onBackClick = { navController.navigateUp() },
            onPopularViewAll = { navController.navigate(Navigation.POPULAR_RECIPES) },
            onEditorsViewAll = { navController.navigate(Navigation.EDITORS_CHOICE) },
            onRecipeClick = { navController.navigate(Navigation.RECIPE_DETAIL) }
        )
    }
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPopularViewAll: () -> Unit = {},
    onEditorsViewAll: () -> Unit = {},
    onRecipeClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Breakfast") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Back button (pijl)
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(48.dp)
        ) {
            Text("←", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = "Search",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        SearchBarComponent(searchQuery) { searchQuery = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Filters (Breakfast, Lunch, Dinner)
        CategoryFilterSection(selectedCategory) { selectedCategory = it }

        Spacer(modifier = Modifier.height(24.dp))

        // Popular Recipes Section
        PopularRecipesSection(onViewAll = onPopularViewAll, onRecipeClick = onRecipeClick)

        Spacer(modifier = Modifier.height(24.dp))

        // Editor's Choice Section
        EditorsChoiceSection(onViewAll = onEditorsViewAll, onRecipeClick = onRecipeClick)
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
        placeholder = { Text("Search", color = Color.Gray) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = Color.Gray
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
    val categories = listOf("Breakfast", "Lunch", "Dinner")

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
                text = "Popular Recipes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All",
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
                    .background(Color.Gray, RoundedCornerShape(12.dp)),
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
        EditorRecipe("Easy homemade beef burger", "James Spader"),
        EditorRecipe("Blueberry with egg for breakfast", "Alice Fala")
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Editor's Choice",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All",
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

data class EditorRecipe(val title: String, val author: String)

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
                    .background(Color.Gray, RoundedCornerShape(12.dp))
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
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Gray, shape = androidx.compose.foundation.shape.CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = recipe.author,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Arrow Button
            IconButton(
                onClick = { /* TODO: Navigate to recipe details */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(BrandOrange, shape = androidx.compose.foundation.shape.CircleShape)
            ) {
                Text("→", color = Color.White, style = MaterialTheme.typography.bodyLarge)
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
