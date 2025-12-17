package models.dto

@kotlinx.serialization.Serializable
data class UserFavouritesEntry(
    val userId: String,
    val recipeId: Int
)