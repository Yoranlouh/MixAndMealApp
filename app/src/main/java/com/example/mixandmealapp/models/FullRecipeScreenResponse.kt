package com.example.mixandmealapp.models

import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.enums.MealType
import com.example.mixandmealapp.models.enums.KitchenStyle
import com.example.mixandmealapp.models.entries.RecipeImageEntry

@kotlinx.serialization.Serializable
data class FullRecipeScreenResponse(
    val id: Int,
    val title: String,
    val description: String,
    val instructions: String,
    val prepTime: Int,
    val cookingTime: Int,
    val difficulty: Difficulty,
    val images: List<RecipeImageEntry>,
    val mealType: MealType,
    val kitchenStyle: KitchenStyle,
    val diets: List<DietEntry>,
    val allergens: List<AllergenEntry>,
    val ingredients: List<IngredientUnitEntry>
)
