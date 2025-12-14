package com.example.mixandmealapp.models

@kotlinx.serialization.Serializable
data class RecipeCardResponse(
    val recipeId: Int,
    val title: String,
    val description: String,
    val cookingTime: Int,
    val imageUrl: List<String>
)