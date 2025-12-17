package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDietEntry(
    val recipeId: Int,
    val dietId: Int
)