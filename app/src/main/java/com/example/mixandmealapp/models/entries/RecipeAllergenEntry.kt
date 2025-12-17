package models.dto

@kotlinx.serialization.Serializable
data class RecipeAllergenEntry(
    val recipeId:Int,
    val allergenId : Int
)