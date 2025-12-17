package models.dto

@kotlinx.serialization.Serializable
data class UserFridgeEntry (
    val userId: String,
    val ingredientName: String
)