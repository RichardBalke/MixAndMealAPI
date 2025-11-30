package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserFridgeEntry (
    val userId: String,
    val ingredientName: String
)