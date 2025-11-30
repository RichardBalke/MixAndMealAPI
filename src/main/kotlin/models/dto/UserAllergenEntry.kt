package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserAllergenEntry(
    val userId: String,
    val allergenId: Int
)