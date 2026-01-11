package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipePreviewCard(
    val recipeId: Int,
    val title: String,
    val imageUrl: String?,
    val cookingTime: Int,
    val description: String
)