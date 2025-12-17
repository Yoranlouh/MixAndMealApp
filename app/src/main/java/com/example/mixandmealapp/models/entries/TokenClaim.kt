package models.dto

@kotlinx.serialization.Serializable
data class TokenClaim(
    val name: String,
    val value: String
)