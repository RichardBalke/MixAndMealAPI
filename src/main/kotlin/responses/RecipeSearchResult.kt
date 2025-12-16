package responses

import kotlinx.serialization.Serializable
import models.dto.RecipeImageEntry

@Serializable
data class RecipeSearchResult(
    val recipeId: Int,
    val title: String,
    val description: String,
    val cookingTime: Int,
    val imageUrl: List<RecipeImageEntry>,
    val score: Double
)
