package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class DietEntry(
    val id: Int,
    val displayName: String,
    val description: String
)