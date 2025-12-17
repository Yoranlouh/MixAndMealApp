package models.dto

@kotlinx.serialization.Serializable
data class UserAllergenEntry(
    val userId: String,
    val allergenId: Int
)