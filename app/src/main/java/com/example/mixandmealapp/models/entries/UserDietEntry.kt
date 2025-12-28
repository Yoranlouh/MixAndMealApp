package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class UserDietEntry (
    val userId: String,
    val dietId: Int,
)