package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserFavouritesEntry(
    val userId: String,
    val recipeId: Int
)