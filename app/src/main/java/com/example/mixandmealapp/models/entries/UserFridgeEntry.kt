package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class UserFridgeEntry (
    val userId: String,
    val ingredientName: String
)