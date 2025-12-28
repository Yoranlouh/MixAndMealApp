package com.example.mixandmealapp.models.entries

import com.example.mixandmealapp.models.enums.Role

@kotlinx.serialization.Serializable
data class UserEntry(
    val name: String,
    val email: String,
    val password: String,
    val role: Role = Role.USER

)