package com.example.mixandmealapp.models.requests

@kotlinx.serialization.Serializable
data class AuthRequest(
    val username: String,
    val password: String,
    val email: String
)
