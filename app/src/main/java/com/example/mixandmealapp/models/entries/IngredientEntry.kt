package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class IngredientEntry(
    val name: String,
    val description: String
)