package com.example.mixandmealapp.models.enums

@kotlinx.serialization.Serializable
enum class MealType(val mealTypeName: String) {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner"),
    DESSERT("dessert");
}