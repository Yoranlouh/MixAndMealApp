package com.example.mixandmealapp.ui.screens.search

import com.example.mixandmealapp.R

// Shared filter option source (mirrors UploadScreen options)
object FilterOptions {

    val kitchenStyles = listOf(
        R.string.upload_kitchen_style_asian,
        R.string.upload_kitchen_style_dutch,
        R.string.upload_kitchen_style_easteurope,
        R.string.upload_kitchen_style_french,
        R.string.upload_kitchen_style_greek,
        R.string.upload_kitchen_style_indian,
        R.string.upload_kitchen_style_italian,
        R.string.upload_kitchen_style_japanese,
        R.string.upload_kitchen_style_korean,
        R.string.upload_kitchen_style_mediterranean,
        R.string.upload_kitchen_style_mexican,
        R.string.upload_kitchen_style_spanish,
        R.string.upload_kitchen_style_thai,
        R.string.upload_kitchen_style_turkish,
        R.string.upload_kitchen_style_vietnamese
    )

    val mealTypes = listOf(
        R.string.upload_meal_type_breakfast,
        R.string.upload_meal_type_lunch,
        R.string.upload_meal_type_dinner,
        R.string.upload_meal_type_dessert,
    )

    val allergens = listOf(
        R.string.upload_allergens_gluten,
        R.string.upload_allergens_shellfish,
        R.string.upload_allergens_eggs,
        R.string.upload_allergens_fish,
        R.string.upload_allergens_peanuts,
        R.string.upload_allergens_soy,
        R.string.upload_allergens_milk,
        R.string.upload_allergens_tree_nuts,
        R.string.upload_allergens_celery,
        R.string.upload_allergens_mustard,
        R.string.upload_allergens_sesame,
        R.string.upload_allergens_sulphites,
        R.string.upload_allergens_lupin,
        R.string.upload_allergens_molluscs,
        R.string.upload_allergens_corn
    )

    val diets = listOf(
        R.string.upload_diets_vegan,
        R.string.upload_diets_vegetarian,
        R.string.upload_diets_gluten_free,
        R.string.upload_diets_lactose_free,
        R.string.upload_diets_nut_free,
        R.string.upload_diets_dairy_free,
        R.string.upload_diets_low_sugar,
        R.string.upload_diets_low_salt,
        R.string.upload_diets_halal,
        R.string.upload_diets_kosher,
        R.string.upload_diets_paleo,
        R.string.upload_diets_flexitarian,
        R.string.upload_diets_raw_food,
        R.string.upload_diets_keto
    )
}

data class Recipe(
    val id: String,
    val title: String,
    val kitchenStyle: String?,
    val mealType: String?,
    val allergens: Set<String> = emptySet(),
    val diets: Set<String> = emptySet(),
    val description: String = "",
    val durationMinutes: Int = 30,
    val difficulty: String = "Easy",
    val imageUrl: String? = null
)

data class SearchArgs(
    val query: String,
    val kitchenStyles: Set<String> = emptySet(),
    val mealTypes: Set<String> = emptySet(),
    val allergens: Set<String> = emptySet(),
    val diets: Set<String> = emptySet()
)

