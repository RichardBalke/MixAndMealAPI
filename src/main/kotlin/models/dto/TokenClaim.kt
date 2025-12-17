package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenClaim(
    val name: String,
    val value: String
)