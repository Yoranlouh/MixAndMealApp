package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class RecipeAllergenEntry(
    val recipeId:Int,
    val allergenId : Int
)