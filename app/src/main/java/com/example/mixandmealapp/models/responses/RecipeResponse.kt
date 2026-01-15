package com.example.mixandmealapp.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResponse(
    val id: Int? = null,
    val message: String? = "Recipe uploaded successfully"
)
