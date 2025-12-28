package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)