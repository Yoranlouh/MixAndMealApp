package models.dto

@kotlinx.serialization.Serializable
data class RecipeDietEntry(
    val recipeId: Int,
    val dietId: Int
)