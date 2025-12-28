package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class DietEntry(
    val id: Int,
    val displayName: String,
    val description: String
)