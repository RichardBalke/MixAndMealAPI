package responses

import kotlinx.serialization.Serializable

@Serializable
data class RecipeSearchResult(
    val recipeId: Int,
    val title: String,
    val description: String,
    val cookingTime: Int,
    val imageUrl: List<String>,
    val score: Double
)
