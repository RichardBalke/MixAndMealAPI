package models.dto

import kotlinx.serialization.Serializable
import models.enums.Difficulty
import models.enums.KitchenStyle
import models.enums.MealType

@Serializable
data class RecipeEntry(
    val id: Int,
    val title: String,
    val description: String,
    val instructions: String,
    val prepTime: Int,
    val cookingTime: Int,
    val difficulty: Difficulty,
    val mealType: MealType,
    val kitchenStyle: KitchenStyle,
    val favoritesCount: Int
)