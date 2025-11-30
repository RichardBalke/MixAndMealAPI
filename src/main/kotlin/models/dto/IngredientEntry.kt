package models.dto

import kotlinx.serialization.Serializable

@Serializable
data class IngredientEntry(
    val name: String,
    val description: String
)