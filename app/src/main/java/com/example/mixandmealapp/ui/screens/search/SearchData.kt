package com.example.mixandmealapp.ui.screens.search

// Shared filter option source (mirrors UploadScreen options)
object FilterOptions {
    val kitchenStyles = listOf(
        "Asian", "Chinese", "Dutch", "East-europe", "French", "Greek",
        "Indian", "Italian", "Japanese", "Korean", "Mediterranean",
        "Mexican", "Spanish", "Thai", "Turkish", "Vietnamese"
    )

    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Dessert")

    val allergens = listOf(
        "Gluten", "Shellfish", "Eggs", "Fish", "Peanuts", "Soy",
        "Milk", "Nuts", "Celery", "Mustard", "Sesame", "Lupin",
        "Sulphites", "Molluscs"
    )

    val diets = listOf(
        "Vegan", "Vegetarian", "Gluten free", "Lactose free",
        "Nut free", "Diary free", "Low sugar", "Low salt", "Halal",
        "Kosher", "Paleo", "Flexitarian", "Raw food", "Keto"
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

object MockRecipeRepository {
    // In-memory mock data
    private val items = listOf(
        Recipe(
            id = "1",
            title = "Spaghetti Carbonara",
            kitchenStyle = "Italian",
            mealType = "Dinner",
            allergens = setOf("Eggs", "Milk"),
            description = "Classic creamy pasta with eggs, cheese and pancetta.",
            durationMinutes = 25,
            difficulty = "Medium"
        ),
        Recipe(
            id = "2",
            title = "Vegan Buddha Bowl",
            kitchenStyle = "Asian",
            mealType = "Lunch",
            diets = setOf("Vegan", "Diary free"),
            description = "Colorful bowl with grains, veggies and tahini dressing.",
            durationMinutes = 20,
            difficulty = "Easy"
        ),
        Recipe(
            id = "3",
            title = "Dutch Pancakes",
            kitchenStyle = "Dutch",
            mealType = "Breakfast",
            allergens = setOf("Eggs", "Milk", "Gluten"),
            description = "Thin Dutch-style pancakes, perfect with syrup.",
            durationMinutes = 30,
            difficulty = "Easy"
        ),
        Recipe(
            id = "4",
            title = "Chicken Tacos",
            kitchenStyle = "Mexican",
            mealType = "Dinner",
            description = "Spiced chicken in soft tortillas with fresh salsa.",
            durationMinutes = 35,
            difficulty = "Easy"
        ),
        Recipe(
            id = "5",
            title = "Sushi Roll",
            kitchenStyle = "Japanese",
            mealType = "Dinner",
            allergens = setOf("Fish"),
            description = "Homemade maki rolls with fresh fish and rice.",
            durationMinutes = 50,
            difficulty = "Hard"
        ),
        Recipe(
            id = "6",
            title = "Greek Salad",
            kitchenStyle = "Greek",
            mealType = "Lunch",
            diets = setOf("Vegetarian", "Low salt"),
            description = "Crisp veggies with feta, olives and oregano.",
            durationMinutes = 15,
            difficulty = "Easy"
        ),
    )

    fun search(args: SearchArgs): List<Recipe> {
        val q = args.query.trim().lowercase()
        return items.filter { r ->
            val matchesQuery = q.isEmpty() ||
                    r.title.lowercase().contains(q) ||
                    (r.kitchenStyle?.lowercase()?.contains(q) == true) ||
                    r.description.lowercase().contains(q)

            val matchesKitchen = args.kitchenStyles.isEmpty() || (r.kitchenStyle != null && args.kitchenStyles.contains(r.kitchenStyle))
            val matchesMeal = args.mealTypes.isEmpty() || (r.mealType != null && args.mealTypes.contains(r.mealType))

            val matchesAllergens = args.allergens.isEmpty() || args.allergens.none { it in r.allergens }
            // Diets: require recipe to include all selected diets (AND). Adjust as needed.
            val matchesDiets = args.diets.isEmpty() || args.diets.all { it in r.diets }

            matchesQuery && matchesKitchen && matchesMeal && matchesAllergens && matchesDiets
        }
    }
}
