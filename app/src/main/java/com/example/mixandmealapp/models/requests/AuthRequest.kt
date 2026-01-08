package com.example.mixandmealapp.models.requests

import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class AuthRequest(
    val username: String,
    val password: String,
    val email: String
)

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String = "USER"
)