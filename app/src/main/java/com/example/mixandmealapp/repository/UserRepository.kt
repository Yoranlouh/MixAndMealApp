package com.example.mixandmealapp.repository

import android.util.Log
import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.entries.UserFridgeEntry
import com.example.mixandmealapp.models.requests.AllergenIDRequest
import com.example.mixandmealapp.models.requests.DietsIDRequest
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
        return try {
            val response: AuthResponse = ApiClient.client.post("http://10.0.2.2:8080/signup") {
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

    suspend fun addDiet(token: String, diet: DietsIDRequest) : List<DietEntry> {
        return ApiService.addDietForUser(token, diet)
    }

    suspend fun removeDiet(token: String, diet: DietsIDRequest) : List<DietEntry> {
        return ApiService.removeDietForUser(token, diet)
    }

    suspend fun getAllergens(token: String?): List<AllergenEntry> {
        return ApiService.getAllergensForUser(token)
    }

    suspend fun addAllergen(token: String, allergen: AllergenIDRequest) : List<AllergenEntry> {
        return ApiService.addAllergenForUser(token, allergen)
    }

    suspend fun removeAllergen(token: String, allergen: AllergenIDRequest) : List<AllergenEntry> {
        return ApiService.removeAllergenForUser(token, allergen)
    }

    suspend fun getAllAllergens(): List<AllergenEntry> {
        return ApiService.getAllAllergens()
    }

    suspend fun getAllDiets(): List<DietEntry> {
        return ApiService.getAllDiets()
    }

    suspend fun getFavourites(token: String?): List<RecipeCardResponse>{
        return ApiService.getFavouritesForUser(token)
    }

    suspend fun toggleFavourite(token: String?, recipeId: RecipeIDRequest): List<RecipeCardResponse>{
        return ApiService.toggleFavourite(token, recipeId)
    }


}

