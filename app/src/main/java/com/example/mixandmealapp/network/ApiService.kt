package com.example.mixandmealapp.network

import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.responses.FullRecipeScreenResponse
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object ApiService {
    private val client = ApiClient.client
    private val domain = "http://10.0.2.2:8080"

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
    suspend fun postLogin(request: Login): Token =
        client.post("$domain/signin") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}

data class Login(
    val email : String,
    val password : String
)

data class Token(
    val token : String
)