package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class RecipeDietEntry(
    val recipeId: Int,
    val dietId: Int
)