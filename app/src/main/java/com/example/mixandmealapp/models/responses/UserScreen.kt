package com.example.mixandmealapp.models.responses

import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.entries.IngredientEntry
import com.example.mixandmealapp.models.entries.RecipeEntry

@kotlinx.serialization.Serializable
class UserScreen (
    val username: String,
    // Recipe should not be all info of a recipe so this should be another response class instead of recipe
    val favorites : Set<RecipeEntry>,
    val allergens : Set<AllergenEntry>,
    val diets : Set<DietEntry>,
    val fridge : Set<IngredientEntry>
)