package com.example.mixandmealapp.repository

import com.example.mixandmealapp.models.RecipeCardResponse
import com.example.mixandmealapp.network.ApiService

class RecipeRepository {
    suspend fun getFeaturedRecipeCard(): RecipeCardResponse {
        return ApiService.getFeaturedRecipe()
    }
}