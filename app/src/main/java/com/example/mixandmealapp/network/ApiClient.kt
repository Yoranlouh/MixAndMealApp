package com.example.mixandmealapp.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

object ApiClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(
            kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            }
        ) }
    }
}
