package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class UserFavouritesEntry(
    val userId: String,
    val recipeId: Int
)