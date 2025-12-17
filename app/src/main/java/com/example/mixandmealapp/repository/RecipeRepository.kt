package com.example.mixandmealapp.repository

import com.example.mixandmealapp.models.responses.FullRecipeScreenResponse
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.network.ApiService

class RecipeRepository {
    suspend fun getFeaturedRecipeCard(): RecipeCardResponse {
        return ApiService.getFeaturedRecipe()
    }

    suspend fun getFullRecipeResponse(id : Int): FullRecipeScreenResponse {
        return ApiService.getFullRecipe(id)
    }
}