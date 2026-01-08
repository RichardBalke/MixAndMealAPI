package api.responses

import kotlinx.serialization.Serializable

@Serializable
data class RoleResponse(
    val role: String,
    val userId: String,
    val userName: String
)
