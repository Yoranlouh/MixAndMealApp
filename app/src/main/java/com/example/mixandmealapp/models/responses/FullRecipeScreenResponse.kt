package com.example.mixandmealapp.models.responses

import com.example.mixandmealapp.models.entries.RecipeImageEntry
import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.enums.KitchenStyle
import com.example.mixandmealapp.models.enums.MealType
import kotlinx.serialization.Serializable
import models.dto.AllergenEntry
import models.dto.DietEntry
import models.dto.IngredientUnitEntry

@Serializable
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