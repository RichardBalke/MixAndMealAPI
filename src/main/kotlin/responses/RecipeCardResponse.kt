package api.responses

import kotlinx.serialization.Serializable

@Serializable
data class RecipeCardResponse(
    val recipeId: Int,
    val title: String,
    val description: String,
    val cookingTime: Int,
    val imageUrl: List<String>
)
