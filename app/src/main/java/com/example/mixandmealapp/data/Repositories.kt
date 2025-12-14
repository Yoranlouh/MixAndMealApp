package com.example.mixandmealapp.data

/**
 * Repository interfaces define the app's data contract. For now we provide
 * in-memory fake implementations. Later, you can replace them with real
 * database/network backed implementations without changing your ViewModels.
 */

interface RecipesRepository {
    suspend fun search(
        query: String,
        kitchens: Set<String> = emptySet(),
        meals: Set<String> = emptySet(),
        allergens: Set<String> = emptySet(),
        diets: Set<String> = emptySet()
    ): List<String>

    // Home/Detail use-cases
    suspend fun getFeatured(): List<String>
    suspend fun getPopular(page: Int = 1): List<String>
    suspend fun getById(id: String): String
}

interface FavouritesRepository {
    suspend fun getFavourites(): List<String>
    suspend fun addFavourite(id: String)
    suspend fun removeFavourite(id: String)
}

interface SettingsRepository {
    suspend fun getSettings(): AppSettings
    suspend fun saveSettings(settings: AppSettings)
    suspend fun isPrivacyAccepted(): Boolean
}

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun register(name: String, email: String, password: String): AuthResult
}

interface SessionRepository {
    suspend fun saveToken(token: String)
    suspend fun clear()
    suspend fun isLoggedIn(): Boolean
}

interface UserRepository {
    suspend fun getProfile(): UserProfile
    suspend fun saveProfile(profile: UserProfile)
}

interface UploadRepository {
    suspend fun upload(title: String, description: String, tags: List<String>, mediaUris: List<String>): Boolean
}

data class FridgeItem(val id: String, val name: String, val quantity: String)

interface FridgeRepository {
    suspend fun getItems(): List<FridgeItem>
    suspend fun addItem(name: String, quantity: String): FridgeItem
    suspend fun removeItem(id: String)
    suspend fun getSuggestions(items: List<FridgeItem>): List<String>
}

// Ingredient catalog for search/autocomplete and creation
data class IngredientDef(val id: String, val name: String, val description: String)

interface IngredientCatalogRepository {
    suspend fun search(prefix: String, limit: Int = 10): List<IngredientDef>
    suspend fun add(name: String, description: String): IngredientDef
}

interface AnalyticsRepository {
    suspend fun getMetrics(period: String): Map<String, Number>
}

/**
 * Centralized metadata for search filters to prevent duplicated lists in UI.
 */
interface SearchMetadataRepository {
    suspend fun getKitchens(): List<String>
    suspend fun getMeals(): List<String>
    suspend fun getAllergens(): List<String>
    suspend fun getDiets(): List<String>
}

// Simple data models used across repositories
data class AppSettings(
    val language: String = "nl",
    val theme: String = "system",
    val allergies: Set<String> = emptySet(),
    val privacyAccepted: Boolean = false
)

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null
)

data class AuthResult(val success: Boolean, val token: String? = null, val error: String? = null)

/**
 * Simple Service Locator to keep things lightweight (no DI yet).
 * Swap these singletons later with real implementations.
 */
object ServiceLocator {
    val recipesRepository: RecipesRepository by lazy { FakeRecipesRepository() }
    val favouritesRepository: FavouritesRepository by lazy { FakeFavouritesRepository() }
    val settingsRepository: SettingsRepository by lazy { FakeSettingsRepository() }
    val authRepository: AuthRepository by lazy { FakeAuthRepository() }
    val sessionRepository: SessionRepository by lazy { InMemorySessionRepository() }
    val userRepository: UserRepository by lazy { FakeUserRepository() }
    val uploadRepository: UploadRepository by lazy { FakeUploadRepository() }
    val fridgeRepository: FridgeRepository by lazy { InMemoryFridgeRepository() }
    val analyticsRepository: AnalyticsRepository by lazy { FakeAnalyticsRepository() }
    val searchMetadataRepository: SearchMetadataRepository by lazy { FakeSearchMetadataRepository() }
    val ingredientCatalogRepository: IngredientCatalogRepository by lazy { InMemoryIngredientCatalog() }
}

/**
 * A tiny in-memory recipe repository that pretends to search.
 */
private class FakeRecipesRepository : RecipesRepository {
    
    private val all = listOf(
        "Pasta Bolognese",
        "Vegan Curry",
        "Chicken Salad",
        "Tomato Soup",
        "Pancakes",
        "Avocado Toast",
        "Spaghetti Carbonara",
        "Sushi Bowl"
    )

    override suspend fun search(
        query: String,
        kitchens: Set<String>,
        meals: Set<String>,
        allergens: Set<String>,
        diets: Set<String>
    ): List<String> {
        // Simulate IO latency so ViewModels already work asynchronously
        kotlinx.coroutines.delay(150)
        // Very naive filter just for now. Extend with filters when needed.
        val q = query.trim().lowercase()
        return if (q.isBlank()) all else all.filter { it.lowercase().contains(q) }
    }

    override suspend fun getFeatured(): List<String> {
        kotlinx.coroutines.delay(120)
        return listOf("Vegan Curry", "Sushi Bowl", "Avocado Toast")
    }

    override suspend fun getPopular(page: Int): List<String> {
        kotlinx.coroutines.delay(120)
        // Return a slice per page just to simulate
        val start = ((page - 1) * 3).coerceAtLeast(0)
        return all.drop(start).take(3)
    }

    override suspend fun getById(id: String): String {
        kotlinx.coroutines.delay(80)
        // For now just echo id if it matches any, else first
        return all.find { it.equals(id, ignoreCase = true) } ?: (all.firstOrNull() ?: id)
    }
}

/**
 * An in-memory favourites repository with a modifiable list.
 */
private class FakeFavouritesRepository : FavouritesRepository {
    // Seed with 6 demo favourites so Account can show up to 4 while the full
    // Favourites screen can show the complete list and both stay in sync.
    private val favs = mutableListOf(
        "Sunny Egg & Toast Avocado",
        "Bowl of noodle with beef",
        "Easy homemade beef burger",
        "Half boiled egg sandwich",
        "Veggie pasta primavera",
        "Spicy chicken tacos"
    )

    override suspend fun getFavourites(): List<String> {
        kotlinx.coroutines.delay(120)
        return favs.toList()
    }

    override suspend fun addFavourite(id: String) {
        kotlinx.coroutines.delay(80)
        if (!favs.contains(id)) favs.add(id)
    }

    override suspend fun removeFavourite(id: String) {
        kotlinx.coroutines.delay(80)
        favs.removeAll { it == id }
    }
}

private class FakeSettingsRepository : SettingsRepository {
    private var current = AppSettings()

    override suspend fun getSettings(): AppSettings {
        kotlinx.coroutines.delay(80)
        return current
    }

    override suspend fun saveSettings(settings: AppSettings) {
        kotlinx.coroutines.delay(120)
        current = settings
    }

    override suspend fun isPrivacyAccepted(): Boolean {
        kotlinx.coroutines.delay(40)
        return current.privacyAccepted
    }
}

private class FakeAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): AuthResult {
        kotlinx.coroutines.delay(150)
        return if (email.isNotBlank() && password.length >= 4)
            AuthResult(true, token = "fake-token-${'$'}email")
        else AuthResult(false, error = "Invalid credentials")
    }

    override suspend fun register(name: String, email: String, password: String): AuthResult {
        kotlinx.coroutines.delay(200)
        return if (name.isNotBlank() && email.contains('@') && password.length >= 6)
            AuthResult(true, token = "fake-token-${'$'}email")
        else AuthResult(false, error = "Invalid data")
    }
}

private class InMemorySessionRepository : SessionRepository {
    @Volatile private var token: String? = null

    override suspend fun saveToken(token: String) {
        kotlinx.coroutines.delay(40)
        this.token = token
    }

    override suspend fun clear() {
        kotlinx.coroutines.delay(40)
        token = null
    }

    override suspend fun isLoggedIn(): Boolean {
        kotlinx.coroutines.delay(30)
        return token != null
    }
}

private class FakeUserRepository : UserRepository {
    private var profile = UserProfile(name = "Demo User", email = "demo@example.com")

    override suspend fun getProfile(): UserProfile {
        kotlinx.coroutines.delay(100)
        return profile
    }

    override suspend fun saveProfile(profile: UserProfile) {
        kotlinx.coroutines.delay(150)
        this.profile = profile
    }
}

private class FakeUploadRepository : UploadRepository {
    override suspend fun upload(
        title: String,
        description: String,
        tags: List<String>,
        mediaUris: List<String>
    ): Boolean {
        kotlinx.coroutines.delay(400)
        return title.isNotBlank()
    }
}

private class InMemoryFridgeRepository : FridgeRepository {
    private val items = mutableListOf<FridgeItem>()

    override suspend fun getItems(): List<FridgeItem> {
        kotlinx.coroutines.delay(60)
        return items.toList()
    }

    override suspend fun addItem(name: String, quantity: String): FridgeItem {
        kotlinx.coroutines.delay(80)
        // Use a UUID to guarantee unique IDs even when adding multiple items within the same millisecond.
        val item = FridgeItem(java.util.UUID.randomUUID().toString(), name, quantity)
        items.add(item)
        return item
    }

    override suspend fun removeItem(id: String) {
        kotlinx.coroutines.delay(60)
        items.removeAll { it.id == id }
    }

    override suspend fun getSuggestions(items: List<FridgeItem>): List<String> {
        kotlinx.coroutines.delay(120)
        // Very naive suggestion logic
        return when {
            items.any { it.name.contains("tomato", true) } -> listOf("Tomato Soup", "Pasta Bolognese")
            items.any { it.name.contains("avocado", true) } -> listOf("Avocado Toast")
            else -> listOf("Vegan Curry")
        }
    }
}

/**
 * Very small in-memory ingredient catalog that supports prefix search and adding new items.
 */
private class InMemoryIngredientCatalog : IngredientCatalogRepository {
    private val items = mutableListOf(
        IngredientDef("1", "paprika", "Sweet pepper, commonly used in salads and stews"),
        IngredientDef("2", "potato", "Starchy tuber, versatile for many dishes"),
        IngredientDef("3", "pepper", "Black pepper spice"),
        IngredientDef("4", "parsley", "Fresh herb for garnish and flavor"),
        IngredientDef("5", "pasta", "Durum wheat noodles in many shapes"),
        IngredientDef("6", "pear", "Sweet fruit"),
        IngredientDef("7", "peach", "Juicy stone fruit"),
        IngredientDef("8", "peanut", "Legume often used as nuts"),
        IngredientDef("9", "onion", "Aromatic bulb used for flavor"),
        IngredientDef("10", "tomato", "Versatile fruit used as vegetable")
    )

    override suspend fun search(prefix: String, limit: Int): List<IngredientDef> {
        kotlinx.coroutines.delay(60)
        val p = prefix.trim().lowercase()
        if (p.isBlank()) return emptyList()
        return items.filter { it.name.lowercase().startsWith(p) }.take(limit)
    }

    override suspend fun add(name: String, description: String): IngredientDef {
        kotlinx.coroutines.delay(80)
        val existing = items.firstOrNull { it.name.equals(name, ignoreCase = true) }
        if (existing != null) return existing
        val def = IngredientDef(System.currentTimeMillis().toString(), name.trim(), description.trim())
        items.add(def)
        return def
    }
}

private class FakeAnalyticsRepository : AnalyticsRepository {
    override suspend fun getMetrics(period: String): Map<String, Number> {
        kotlinx.coroutines.delay(120)
        return mapOf(
            "activeUsers" to 1234,
            "recipesViewed" to 5678,
            "uploads" to 42
        )
    }
}

private class FakeSearchMetadataRepository : SearchMetadataRepository {
    override suspend fun getKitchens(): List<String> {
        kotlinx.coroutines.delay(60)
        return listOf("Italiaans", "Indisch", "Japans", "Mexicaans", "Nederlands")
    }

    override suspend fun getMeals(): List<String> {
        kotlinx.coroutines.delay(60)
        return listOf("Ontbijt", "Lunch", "Diner", "Snack", "Dessert")
    }

    override suspend fun getAllergens(): List<String> {
        kotlinx.coroutines.delay(60)
        return listOf("Gluten", "Noten", "Lactose", "Soja", "Schaaldieren")
    }

    override suspend fun getDiets(): List<String> {
        kotlinx.coroutines.delay(60)
        return listOf("Vegan", "Vegetarisch", "Keto", "Paleo", "Halal")
    }
}
