package responses

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String
)
