package api.requests

import kotlinx.serialization.Serializable

@Serializable
data class RecipeIDRequest(
    val recipeId: Int
)
