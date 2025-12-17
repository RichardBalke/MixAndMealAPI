package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)