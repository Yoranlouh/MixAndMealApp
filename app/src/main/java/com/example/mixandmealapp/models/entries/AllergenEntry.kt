package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class AllergenEntry(
    val id: Int,
    val name: String,
    val displayName: String,
    val description: String
)