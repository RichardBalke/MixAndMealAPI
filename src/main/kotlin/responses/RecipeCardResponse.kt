package api.responses

import kotlinx.serialization.Serializable
import models.dto.RecipeImageEntry

@Serializable
data class RecipeCardResponse(
    val recipeId: Int,
    val title: String,
    val description: String,
    val cookingTime: Int,
    val imageUrl: MutableList<RecipeImageEntry>
)
