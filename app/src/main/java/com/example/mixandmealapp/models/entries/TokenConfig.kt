package models.dto

@kotlinx.serialization.Serializable
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)