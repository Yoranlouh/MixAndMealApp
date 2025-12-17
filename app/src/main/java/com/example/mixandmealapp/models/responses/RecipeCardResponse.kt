package com.example.mixandmealapp.models.responses

import com.example.mixandmealapp.models.entries.RecipeImageEntry
import kotlinx.serialization.Serializable

@Serializable
data class RecipeCardResponse(
    val recipeId: Int,
    val title: String,
    val description: String,
    val cookingTime: Int,
    val imageUrl: List<RecipeImageEntry>
)