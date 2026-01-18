package com.example.mixandmealapp.repository

import com.example.mixandmealapp.models.entries.UserFridgeEntry
import com.example.mixandmealapp.models.requests.IngredientIDRequest
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.network.ApiService
import com.example.mixandmealapp.ui.navigation.AppNavigation

class FridgeRepository {

    suspend fun getFridgeItems(token: String?
    ): List<UserFridgeEntry>{
        return ApiService.getFridge(token)
    }

    suspend fun addIngredientToFridge(token: String?, ingredientId: IngredientIDRequest
    ): List<UserFridgeEntry>{
        return ApiService.addIngredientToFridge(token, ingredientId)
    }

    suspend fun removeIngredientFromFridge(token: String?,ingredientId: IngredientIDRequest
    ): List<UserFridgeEntry>{
        return ApiService.removeIngredientFromFridge(token, ingredientId)
    }
}