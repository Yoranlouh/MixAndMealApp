package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import com.example.mixandmealapp.models.entries.RecipeImageEntry
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow

import com.example.mixandmealapp.ui.components.FavoriteIcon


@Composable
fun PopularRecipeCard(
    recipe: RecipeCardResponse,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isFavorite: Boolean? = null,
    onToggleFavorite: (() -> Unit)? = null
) {

    Card(
        modifier = Modifier
            .size(160.dp, 240.dp)
            .then(modifier)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween // Use arrangement to push items apart
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .background(Color.Gray)
                ) {
                    val imageUrl = recipe.imageUrl.firstOrNull()?.imageUrl
                        ?: "https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg"
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Favorite icon in the top-right corner (same style as favourites screen)
                    // If a controlled state is provided, use it; otherwise remember local state
                    val localFav = remember { mutableStateOf(false) }
                    val favState = isFavorite ?: localFav.value
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        FavoriteIcon(isFavorite = favState) {
                            if (onToggleFavorite != null) {
                                onToggleFavorite()
                            } else {
                                localFav.value = !localFav.value
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = recipe.title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                val rawDesc = recipe.description?.trim().orEmpty()
                val maxChars = 140
                val desc = if (rawDesc.length > maxChars) rawDesc.take(maxChars).trimEnd() + " .." else rawDesc
                if (desc.isNotEmpty()) {
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 3,
                        overflow = TextOverflow.Clip
                    )
                }
            }
            Text("Cooking time: ${recipe.cookingTime} min", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PopularRecipeCard(
    title: String,
    description: String? = null,
    cookingTimeMinutes: Int? = null,
    imageUrl: String? = null,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    isFavorite: Boolean? = null,
    onToggleFavorite: (() -> Unit)? = null
) {
    // Build a lightweight RecipeCardResponse to reuse the core UI implementation
    val recipe = RecipeCardResponse(
        recipeId = -1,
        title = title,
        description = description ?: "",
        cookingTime = cookingTimeMinutes ?: 0,
        imageUrl = listOf(
            RecipeImageEntry(
                id = -1,
                recipeId = -1,
                imageUrl = imageUrl ?: "https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg"
            )
        )
    )

    PopularRecipeCard(
        recipe = recipe,
        onClick = onClick,
        modifier = modifier,
        isFavorite = isFavorite,
        onToggleFavorite = onToggleFavorite
    )
}

@Preview(showBackground = true)
@Composable
fun PopularRecipeCardPreview() {
    val sample = RecipeCardResponse(
        recipeId = 1,
        title = "Pasta Bolognese",
        description = "Een heerlijke klassieke pasta met rijke tomatensaus.",
        cookingTime = 30,
        imageUrl = listOf(
            RecipeImageEntry(
                id = 1,
                recipeId = 1,
                imageUrl = "https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg"
            )
        )
    )

    MixAndMealAppTheme {
        PopularRecipeCard(recipe = sample)
            }
            Text("cooking time: ${recipe.cookingTime} min", style = MaterialTheme.typography.bodySmall)
        }
    }
}
