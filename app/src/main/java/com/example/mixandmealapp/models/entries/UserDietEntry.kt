package models.dto

@kotlinx.serialization.Serializable
data class UserDietEntry (
    val userId: String,
    val dietId: Int,
)