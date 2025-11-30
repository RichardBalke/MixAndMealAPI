package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class IngredientUnitEntry(
    val recipeId: Int,
    val ingredientName: String,
    val amount: Double,
    val unitType: String
)