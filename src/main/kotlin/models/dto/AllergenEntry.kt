package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class AllergenEntry(
    val id: Int,
    val name: String,
    val displayName: String,
    val description: String
)