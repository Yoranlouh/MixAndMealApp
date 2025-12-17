package requests

@kotlinx.serialization.Serializable
data class RecipeSearchRequest(
    val partialTitle: String,
    val difficulty: String,
    val mealType: String,
    val kitchenStyle: String,
    val maxCookingTime: Int,
    val diets : List<String>,
    val allergens: List<Int>,
    val ingredients: List<String>
    )
