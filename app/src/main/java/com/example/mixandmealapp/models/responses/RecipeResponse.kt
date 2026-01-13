package com.example.mixandmealapp.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    val id: Int,
    val message: String? = "Recipe uploaded successfully"
)
