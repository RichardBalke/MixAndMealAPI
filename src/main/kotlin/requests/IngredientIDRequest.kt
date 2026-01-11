package api.requests

import kotlinx.serialization.Serializable

@Serializable
data class IngredientIDRequest(
    val ingredientName: String
)
