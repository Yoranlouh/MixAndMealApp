package com.example.mixandmealapp.network

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object ApiService {
    private val client = ApiClient.client
    private val domain = "http://localhost:8080"

    suspend fun getExample(): String =
        client.get("$domain/api/example").body()

    suspend fun postLogin(request: Login): Token =
        client.post("$domain/api/login") {
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