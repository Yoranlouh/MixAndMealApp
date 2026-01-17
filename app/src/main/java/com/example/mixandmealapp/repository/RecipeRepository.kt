package com.example.mixandmealapp.repository

import android.net.Uri
import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.requests.RecipeUploadRequest
import com.example.mixandmealapp.models.responses.FullRecipeScreenResponse
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.models.responses.RecipeResponse
import com.example.mixandmealapp.network.ApiService
import io.ktor.client.statement.HttpResponse

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

    suspend fun getAllRecipes(): List<RecipeCardResponse>{
        return ApiService.getAllRecipes()
    }

    suspend fun uploadRecipe(token: String?, imageUri: String?, request: RecipeUploadRequest, recipeId: Int?): RecipeResponse {
        return ApiService.uploadRecipe(token, imageUri, request)
    }

    suspend fun deleteRecipe(token: String?, recipeId: Int): HttpResponse {
        return ApiService.deleteRecipe(token, recipeId)
    }

    suspend fun searchRecipes(
        query: String,
        kitchens: Set<String>,
        meals: Set<String>,
        allergens: Set<String>,
        diets: Set<String>
    ): List<RecipeCardResponse> {
        val queryParams = mutableMapOf<String, String>()
        if (query.isNotBlank()) queryParams["query"] = query
        // Assuming your backend expects a single value for these filters
        if (kitchens.isNotEmpty()) queryParams["kitchen"] = kitchens.joinToString(",")
        if (meals.isNotEmpty()) queryParams["meal"] = meals.joinToString(",")
        if (allergens.isNotEmpty()) queryParams["allergens"] = allergens.joinToString(",")
        if (diets.isNotEmpty()) queryParams["diets"] = diets.joinToString(",")

        return ApiService.searchRecipes(queryParams)
    }

}