package models.dto

import api.models.Role
import kotlinx.serialization.Serializable

@Serializable
data class UserEntry(
    val name: String,
    val email: String,
    val password: String,
    val role: Role = Role.USER

)