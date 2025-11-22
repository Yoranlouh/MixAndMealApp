package com.example.mixandmealapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.components.BottomNavBar
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.theme.BrandGreen
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.BrandYellow
import com.example.mixandmealapp.ui.theme.DarkText
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Header()
        Spacer(modifier = Modifier.height(24.dp))
        FeaturedSection()
        Spacer(modifier = Modifier.height(24.dp))
        CategorySection()
        Spacer(modifier = Modifier.height(24.dp))
        PopularRecipesSection()
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.WbSunny, contentDescription = "Sun icon", tint = BrandYellow)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = "Hello", style = MaterialTheme.typography.bodyLarge)
            }
            Text(text = "Richard", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = { /* TODO: Handle cart click */ }) {
            Icon(Icons.Filled.ShoppingCart, contentDescription = "Shopping Cart")
        }
    }
}

@Composable
fun FeaturedSection() {
    Column {
        Text(
            text = "Featured",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Placeholder for the large featured card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().background(BrandYellow.copy(alpha = 0.8f))) {
                Text("Featured Recipe Card", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun CategorySection() {
    val categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert")
    var selectedCategory by remember { mutableStateOf("Breakfast") }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = "See All", style = MaterialTheme.typography.bodyMedium, color = BrandOrange)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Button(
                    onClick = { selectedCategory = category },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) BrandOrange else BrandGrey,
                        contentColor = if (isSelected) Color.White else DarkText
                    )
                ) {
                    Text(category)
                }
            }
        }
    }
}

@Composable
fun PopularRecipesSection() {
    val recipes = listOf("Taco Salad", "Pancakes")
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
            Text(text = "See All", style = MaterialTheme.typography.bodyMedium, color = BrandOrange)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recipes) { recipe ->
                PopularRecipeCard(recipe)
            }
        }
    }
}

@Composable
fun PopularRecipeCard(recipe: String) {
    Card(
        modifier = Modifier.size(160.dp, 240.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = recipe, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            Spacer(modifier = Modifier.weight(1f))
            Text("120 Kcal | 20 Min", style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MixAndMealAppTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                BottomNavBar(
                    navController = navController,
                    currentDestination = navController.currentDestination
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Does nothing in preview */ },
                    shape = CircleShape,
                    containerColor = BrandGreen,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.DocumentScanner, "Scan a recipe")
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                HomeScreen(navController = navController)
            }
        }
    }
}