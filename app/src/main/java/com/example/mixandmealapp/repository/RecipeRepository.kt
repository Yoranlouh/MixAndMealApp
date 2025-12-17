package com.example.mixandmealapp.repository

import com.example.mixandmealapp.models.enums.Difficulty
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

    suspend fun getPopularRecipesResponse(limit: Int): List<RecipeCardResponse> {
        return ApiService.getPopularRecipes(limit)
    }
    suspend fun getEasyRecipesResponse(limit: Int): List<RecipeCardResponse> {
        val difficulty= Difficulty.EASY
        return ApiService.getRecipesByDificulty(limit, difficulty)
    }
    suspend fun getMediumRecipesResponse(limit: Int): List<RecipeCardResponse> {
        val difficulty= Difficulty.MEDIUM
        return ApiService.getRecipesByDificulty(limit, difficulty)
    }
    suspend fun getHardRecipesResponse(limit: Int): List<RecipeCardResponse> {
        val difficulty= Difficulty.HARD
        return ApiService.getRecipesByDificulty(limit, difficulty)
    }
    suspend fun getQuickRecipesResponse(limit: Int): List<RecipeCardResponse> {
        return ApiService.getQuickRecipes(limit)
    }
}