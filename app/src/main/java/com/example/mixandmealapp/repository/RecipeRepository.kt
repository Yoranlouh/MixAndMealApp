package com.example.mixandmealapp.repository

import android.net.Uri
import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.requests.RecipeSearchRequest
import com.example.mixandmealapp.models.requests.RecipeUploadRequest
import com.example.mixandmealapp.models.responses.FullRecipeScreenResponse
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.models.responses.RecipeResponse
import com.example.mixandmealapp.network.ApiService
import io.ktor.client.statement.HttpResponse
import java.io.File

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

    suspend fun uploadRecipe(token: String?, request: RecipeUploadRequest): RecipeResponse {
        return ApiService.updateRecipe(token, request)
    }

    suspend fun deleteRecipe(token: String?, recipeId: Int): HttpResponse {
        return ApiService.deleteRecipe(token, recipeId)
    }

    suspend fun searchRecipeRequest(request: RecipeSearchRequest) : List<RecipeCardResponse>{
        return ApiService.recipeSearchRequest(request)
    }

}