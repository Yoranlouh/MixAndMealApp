package com.example.mixandmealapp.network

import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.entries.TokenClaim
import com.example.mixandmealapp.models.entries.UserAllergenEntry
import com.example.mixandmealapp.models.entries.UserDietEntry
import com.example.mixandmealapp.models.entries.UserFridgeEntry
import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.enums.Role
import com.example.mixandmealapp.models.requests.AllergenIDRequest
import com.example.mixandmealapp.models.requests.DietsIDRequest
import com.example.mixandmealapp.models.requests.IngredientIDRequest
import com.example.mixandmealapp.models.requests.Login
import com.example.mixandmealapp.models.requests.RecipeIDRequest
import com.example.mixandmealapp.models.requests.RecipeUploadRequest
import com.example.mixandmealapp.models.responses.AuthResponse
import com.example.mixandmealapp.models.responses.FullRecipeScreenResponse
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.models.responses.RecipeResponse
import com.example.mixandmealapp.models.responses.RoleResponse
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import java.io.File

object ApiService {
    private val client = ApiClient.client
    private val domain = "http://10.0.2.2:8080"

    suspend fun getAllRecipes(): List<RecipeCardResponse> =
        client.get("$domain/recipes").body()

    suspend fun getFeaturedRecipe(): RecipeCardResponse =
        client.get("$domain/recipes/featured/1").body()

    suspend fun getFullRecipe(id: Int): FullRecipeScreenResponse =
        client.get("$domain/fullrecipe/$id").body()

    suspend fun getPopularRecipes(limit: Int): List<RecipeCardResponse> =
        client.get("$domain/popular-recipes/$limit").body()

    suspend fun getRecipesByDificulty(
        limit: Int,
        difficulty: Difficulty
    ): List<RecipeCardResponse> =
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
        client.get("$domain/user/diets") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun addDietForUser(token: String?, diet: DietsIDRequest): List<DietEntry> =
        client.post("$domain/user/diets") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(diet)
        }.body()

    suspend fun removeDietForUser(token: String?, diet: DietsIDRequest): List<DietEntry> =
        client.delete("$domain/user/diets") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(diet)
        }.body()

    suspend fun getAllergensForUser(token: String?): List<AllergenEntry> =
        client.get("$domain/user/allergens") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun addAllergenForUser(token: String?, allergen: AllergenIDRequest): List<AllergenEntry> =
        client.post("$domain/user/allergens") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(allergen)
        }.body()

    suspend fun removeAllergenForUser(
        token: String?,
        allergen: AllergenIDRequest
    ): List<AllergenEntry> =
        client.delete("$domain/user/allergens") {
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

    suspend fun toggleFavourite(
        token: String?,
        recipeId: RecipeIDRequest
    ): List<RecipeCardResponse> =
        client.post("$domain/favourites/add-remove-favourite-recipe") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(recipeId)
        }.body()

    suspend fun getFridge(token: String?): List<UserFridgeEntry> =
        client.get("$domain/fridge") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun addIngredientToFridge(
        token: String?,
        ingredientId: IngredientIDRequest
    ): List<UserFridgeEntry> =
        client.post("$domain/fridge/add-ingredient") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(ingredientId)
        }.body()

    suspend fun removeIngredientFromFridge(
        token: String?,
        ingredientId: IngredientIDRequest
    ): List<UserFridgeEntry> =
        client.delete("$domain/fridge/remove-ingredient-from-fridge") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(ingredientId)
        }.body()

    suspend fun uploadRecipe(
        token: String?,
        imageUri: String?,
        request: RecipeUploadRequest,
        recipeId: Int? = null
    ): RecipeResponse {
        return client.post("$domain/recipes") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()!!
    }

    suspend fun updateRecipe(
        token: String?,
        recipe: RecipeUploadRequest,
        images: List<File>
    ): RecipeResponse {
        return client.submitFormWithBinaryData(
            url = "$domain/update-recipe",
            formData = formData {
                // JSON part
                append(
                    "recipe",
                    Json.encodeToString(recipe),
                    Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    }
                )
                // Image parts
                images.forEachIndexed { index, bytes ->
                    append(
                        "file$index", // name doesn't matter for your backend
                        bytes.readBytes(),
                        Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"image_$index.jpg\"")
                        }
                    )
                }
            }
        ) {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun deleteRecipe(token: String?, recipeId: Int) : HttpResponse =
        client.delete("$domain/delete-recipe") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(RecipeIDRequest(recipeId))
        }.body()


    suspend fun searchRecipes(queryParams: Map<String, String>): List<RecipeCardResponse> =
        client.get("$domain/search") {
            url {
                queryParams.forEach { (key, value) ->
                    if (value.isNotBlank()) { // Ensure not to send empty parameters
                        parameters.append(key, value)
                    }
                }
            }
        }.body()

}
