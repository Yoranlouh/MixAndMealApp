package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class TokenClaim(
    val name: String,
    val value: String
)