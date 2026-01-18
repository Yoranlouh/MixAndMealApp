package com.example.mixandmealapp.models.entries

@kotlinx.serialization.Serializable
data class UserDietEntry (
    val Id: Int,
    val dietName: String,
    val description: String
)