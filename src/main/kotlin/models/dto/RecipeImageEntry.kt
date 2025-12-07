package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeImageEntry (
    val id: Int,
    val recipeId: Int,
    val imageUrl: String
)