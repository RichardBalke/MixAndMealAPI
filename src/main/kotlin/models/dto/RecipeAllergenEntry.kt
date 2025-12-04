package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class RecipeAllergenEntry(
    val recipeId:Int,
    val allergenId : Int
)