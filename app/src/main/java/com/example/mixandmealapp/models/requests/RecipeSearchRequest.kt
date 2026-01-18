package com.example.mixandmealapp.models.requests

@kotlinx.serialization.Serializable
data class RecipeSearchRequest(
    val partialTitle: String?,
    val difficulty: String?,
    val mealType: String?,
    val kitchenStyle: String?,
    val maxCookingTime: Int?,
    val diets : List<Int>,
    val allergens: List<Int>,
    val ingredients: List<String>
    )

fun getDietId(dietName:String) : Int {
    var result = 0
    when(dietName) {
        "Vegan" -> result = 1
        "Vegetarian", "Vegetarisch" -> result = 2
        "Gluten free", "Gluten vrij" -> result = 3
        "Lactose free", "Lactose vrij" -> result = 4
        "Nut free", "Noten vrij" -> result = 5
        "Dairy free", "Zuivel vrij" -> result = 6
        "Low sugar", "Laag suikergehalte" -> result = 7
        "Low sodium", "Weinig zout" -> result = 8
        "Halal" -> result = 9
        "Kosher" -> result = 10
        "Paleo" -> result = 11
        "Keto" -> result = 12
        "Raw food", "Rauw" -> result = 13
        "Flexitarian", "Flexitariër" -> result = 14
    }
    return result
}

fun getAllergenId(allergenName:String) : Int {
    var result = 0
    when(allergenName) {
        "Gluten" -> result = 1
        "Crustaceans", "Schaaldieren" -> result = 2
        "Eggs", "Ei" -> result = 3
        "Fish", "Vis" -> result = 4
        "Peanuts", "Pinda's" -> result = 5
        "Soy", "Soya" -> result = 6
        "Milk", "Melk" -> result = 7
        "Tree Nuts", "Boomnoten" -> result = 8
        "Celery", "Selderij" -> result = 9
        "Mustard", "Mosterd" -> result = 10
        "Sesame", "Sesam" -> result = 11
        "Sulphites", "Sulfieten" -> result = 12
        "Lupin", "Wolvin" -> result = 13
        "Molluscs", "Weekdieren" -> result = 14
        "Corn", "Maïs" -> result = 15
    }
    return result
}
