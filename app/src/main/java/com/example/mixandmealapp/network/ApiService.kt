package com.example.mixandmealapp.network

import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.entries.TokenClaim
import com.example.mixandmealapp.models.entries.UserFridgeEntry
import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.enums.Role
import com.example.mixandmealapp.models.requests.IngredientIDRequest
import com.example.mixandmealapp.models.requests.Login
import com.example.mixandmealapp.models.requests.RecipeIDRequest
import com.example.mixandmealapp.models.responses.AuthResponse
import com.example.mixandmealapp.models.responses.FullRecipeScreenResponse
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.models.responses.RoleResponse
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object ApiService {
    private val client = ApiClient.client
    private val domain = "http://10.0.2.2:8080"

    suspend fun getAllRecipes(): List<RecipeCardResponse> =
        client.get("$domain/recipes").body()
    suspend fun getFeaturedRecipe(): RecipeCardResponse =
        client.get("$domain/recipes/featured/1").body()

    suspend fun getFullRecipe(id : Int) : FullRecipeScreenResponse =
        client.get("$domain/fullrecipe/$id").body()
    suspend fun getPopularRecipes(limit: Int): List<RecipeCardResponse> =
        client.get("$domain/popular-recipes/$limit").body()

    suspend fun getRecipesByDificulty(limit: Int, difficulty: Difficulty): List<RecipeCardResponse> =
        client.get("$domain/recipes/$difficulty/$limit").body()

    suspend fun getQuickRecipes(limit: Int): List<RecipeCardResponse> =
        client.get("$domain/quick-recipes/$limit").body()

    suspend fun postLogin(request: Login): AuthResponse =
        client.post("$domain/signin") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()


    // Change return type back to RoleResponse
    suspend fun checkRole(token: String?): RoleResponse =
        client.get("$domain/authenticate") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getDietsForUser(token: String?): List<DietEntry> =
        client.get("$domain/user-diets") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun addDietForUser(token: String?, diet: DietEntry): List<DietEntry> =
        client.post("$domain/user-diets/add-diet"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(diet)
        }.body()

    suspend fun removeDietForUser(token: String?, diet: DietEntry) : List<DietEntry> =
        client.delete("$domain/user-diets/remove-diet"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(diet)
        }.body()

    suspend fun getAllergensForUser(token: String?): List<AllergenEntry> =
        client.get("$domain/user-allergens") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun addAllergenForUser(token: String?, allergen: AllergenEntry): List<AllergenEntry> =
        client.post("$domain/user-allergens/add-allergen"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(allergen)
        }.body()

    suspend fun removeAllergenForUser(token: String?, allergen: AllergenEntry) : List<AllergenEntry> =
        client.delete("$domain/user-allergens/remove-allergen"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(allergen)
        }.body()

    suspend fun getAllAllergens(): List<AllergenEntry> =
        client.get("$domain/allergens").body()

    suspend fun getAllDiets(): List<DietEntry> =
        client.get("$domain/diets").body()

    suspend fun getFavouritesForUser(token: String?): List<RecipeCardResponse> =
        client.get("$domain/favourites") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun toggleFavourite(token: String?, recipeId: RecipeIDRequest): List<RecipeCardResponse> =
        client.post("$domain/favourites/add-remove-favourite-recipe"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(recipeId)
        }.body()

    suspend fun getFridge(token: String?): List<UserFridgeEntry> =
        client.get("$domain/fridge") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun addIngredientToFridge(token: String?, ingredientId: IngredientIDRequest): List<RecipeCardResponse> =
        client.post("$domain/user-diets/add-diet"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(ingredientId)
        }.body()

    suspend fun removeIngredientFromFridge(token: String?, ingredientId: IngredientIDRequest): List<RecipeCardResponse> =
        client.delete("$domain/user-diets/remove-diet"){
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(ingredientId)
        }.body()

}

