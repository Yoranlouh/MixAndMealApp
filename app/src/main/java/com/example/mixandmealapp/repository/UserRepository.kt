package com.example.mixandmealapp.repository

import android.util.Log
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.entries.UserFridgeEntry
import com.example.mixandmealapp.models.requests.IngredientIDRequest
import com.example.mixandmealapp.models.requests.Login
import com.example.mixandmealapp.models.requests.RecipeIDRequest
import com.example.mixandmealapp.models.responses.AuthResponse
import com.example.mixandmealapp.models.responses.RoleResponse
import com.example.mixandmealapp.network.ApiService
import com.example.mixandmealapp.models.requests.RegisterRequest
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.network.ApiClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType


// example of how to use the ApiClient and ApiService
class UserRepository() {
    suspend fun login(email: String, password: String): AuthResponse {
        return ApiService.postLogin(Login(email, password))
    }

    suspend fun checkRole(token: String): RoleResponse? {
        val response = ApiService.checkRole(token)
        Log.d("UserRepo", "RoleResponse: ${response}")
        return response
    }

    suspend fun register(username: String, email: String, password: String): AuthResponse {
        val url = "http://10.0.2.2:8080/signup"
        return try {
            val response: AuthResponse = ApiClient.client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username, email, password))
            }.body()
            response
        } catch (e: Exception) {
            Log.e("UserRepository", "Registration failed: ${e.message}", e)
            throw e
        }
    }

    suspend fun getDiets(token: String?): List<DietEntry> {
        return ApiService.getDietsForUser(token)
    }

    suspend fun addDiet(token: String, diet: DietEntry) : List<DietEntry> {
        return ApiService.addDietForUser(token, diet)
    }

    suspend fun removeDiet(token: String, diet: DietEntry) : List<DietEntry> {
        return ApiService.removeDietForUser(token, diet)
    }

    suspend fun getFavourites(token: String?): List<RecipeCardResponse>{
        return ApiService.getFavouritesForUser(token)
    }

    suspend fun toggleFavourite(token: String?, recipeId: RecipeIDRequest): List<RecipeCardResponse>{
        return ApiService.toggleFavourite(token, recipeId)
    }

    suspend fun getFridge(token: String?): List<UserFridgeEntry> {
        return ApiService.getFridge(token)
    }

    suspend fun addIngredientToFridge(token: String, ingredientId: IngredientIDRequest) : List<RecipeCardResponse> {
        return ApiService.addIngredientToFridge(token, ingredientId)
    }

    suspend fun removeIngredientFromFridge(token: String, ingredientId: IngredientIDRequest) : List<RecipeCardResponse> {
        return ApiService.removeIngredientFromFridge(token, ingredientId)
    }

}

