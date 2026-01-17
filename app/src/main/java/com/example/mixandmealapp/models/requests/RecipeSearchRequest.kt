package com.example.mixandmealapp.models.requests

@kotlinx.serialization.Serializable
data class RecipeSearchRequest(
    val partialTitle: String,
    val difficulty: String,
    val mealType: String,
    val kitchenStyle: String,
    val maxCookingTime: Int,
    val diets : List<String>,
    val allergens: List<String>,
    val ingredients: List<String>
    )
