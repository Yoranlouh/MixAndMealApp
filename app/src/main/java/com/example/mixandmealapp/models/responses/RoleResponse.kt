package com.example.mixandmealapp.models.responses

@kotlinx.serialization.Serializable
data class RoleResponse(
    val role: String,
    val userId: String,
    val userName: String
)
