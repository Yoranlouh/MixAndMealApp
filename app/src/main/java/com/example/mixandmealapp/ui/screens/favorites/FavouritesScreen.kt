package com.example.mixandmealapp.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.stringResource
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.PopularRecipeCard
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import com.example.mixandmealapp.R
import com.example.mixandmealapp.models.requests.RecipeIDRequest
import com.example.mixandmealapp.ui.viewmodel.FavouritesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    onItemClick: (Int) -> Unit = {},
    navController: NavHostController,
    viewModel: FavouritesViewModel = koinViewModel()
) {
    LaunchedEffect(viewModel) { viewModel.load() }
    val items = viewModel.uiState.favourites
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BackButton(
                            navController = navController,
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = {
                                navController.navigate(com.example.mixandmealapp.ui.navigation.Navigation.HOME) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                        Text(
                            text = stringResource(id = R.string.favourites),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Render items as a two-column grid using rows of two
            val rows = items.chunked(2)
            items(count = rows.size) { rowIndex ->
                val row = rows[rowIndex]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FavoriteRecipeCardItem(
                        id = row[0].recipeId,
                        title = row[0].title,
                        modifier = Modifier.weight(1f),
                        onClick = { onItemClick(row[0].recipeId) },
                        onToggleFavorite = { viewModel.toggleFavourite(RecipeIDRequest(row[0].recipeId)) }
                    )
                    if (row.size > 1) {
                        FavoriteRecipeCardItem(
                            id = row[1].recipeId,
                            title = row[1].title,
                            modifier = Modifier.weight(1f),
                            onClick = { onItemClick(row[1].recipeId) },
                            onToggleFavorite = { viewModel.toggleFavourite(RecipeIDRequest(row[1].recipeId)) }
                        )
                    } else {
                        Box(modifier = Modifier.weight(1f)) {}
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteRecipeCardItem(
    id: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onToggleFavorite: () -> Unit = {}
) {
    // Delegate to the unified recipe card component so styling stays consistent
    PopularRecipeCard(
        title = title,
        description = null,
        cookingTimeMinutes = null,
        imageUrl = null,
        onClick = onClick,
        modifier = modifier
            .height(240.dp),
        isFavorite = true,
        onToggleFavorite = onToggleFavorite
    )
}

@Preview(showBackground = true)
@Composable
fun FavouritesScreenPreview() {
    MixAndMealAppTheme {
        FavouritesScreen(navController = rememberNavController())
    }
}