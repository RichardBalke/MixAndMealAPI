package responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)

@Serializable
data class RoleResponse(
    val role: String
)
