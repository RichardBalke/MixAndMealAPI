package responses

import kotlinx.serialization.Serializable

@Serializable
data class RecipeSearchResult(
    val recipeId: Int,
    val title: String,
    val imageUrl: ByteArray?
)
