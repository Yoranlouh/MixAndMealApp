package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class UserAllergenEntry(
    val userId: String,
    val allergenId: Int
)