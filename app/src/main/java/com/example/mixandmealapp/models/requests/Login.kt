package requests

@kotlinx.serialization.Serializable
data class Login(val email: String, val password: String)