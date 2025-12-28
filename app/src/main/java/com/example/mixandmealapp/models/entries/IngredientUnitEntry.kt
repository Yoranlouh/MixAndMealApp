package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class IngredientUnitEntry(
    val recipeId: Int,
    val ingredientName: String,
    val amount: Double,
    val unitType: String
)