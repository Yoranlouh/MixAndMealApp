package models.dto

@kotlinx.serialization.Serializable
data class IngredientEntry(
    val name: String,
    val description: String
)