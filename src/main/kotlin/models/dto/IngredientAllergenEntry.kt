package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class IngredientAllergenEntry(
    val ingredientName:String,
    val allergenId : Int
)