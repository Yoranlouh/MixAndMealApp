package responses

import com.example.mixandmealapp.models.entries.RecipeImageEntry


@kotlinx.serialization.Serializable
data class RecipeSearchResult(
    val recipeId: Int,
    val title: String,
    val description: String,
    val cookingTime: Int,
    val imageUrl: List<RecipeImageEntry>,
    val score: Double
)
