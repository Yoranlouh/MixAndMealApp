package models.dto

import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.enums.KitchenStyle
import com.example.mixandmealapp.models.enums.MealType

@kotlinx.serialization.Serializable
data class RecipeEntry(
    val id: Int,
    val title: String,
    val description: String,
    val instructions: String,
    val prepTime: Int,
    val cookingTime: Int,
    val difficulty: Difficulty,
    val mealType: MealType,
    val kitchenStyle: KitchenStyle,
    val favoritesCount: Int
)