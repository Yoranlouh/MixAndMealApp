package models.dto

import api.models.Role

@kotlinx.serialization.Serializable
data class UserEntry(
    val name: String,
    val email: String,
    val password: String,
    val role: Role = Role.USER

)