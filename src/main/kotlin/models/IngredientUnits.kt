package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class IngredientUnit(
    val recipeId: Int,
    val ingredientName: String,
    val amount: Double,
    val unitType: String
)

object IngredientUnits : Table() {
    val recipeId = integer("recipe_id")
    val ingredientName = varchar("ingredient_name", 255)
    val amount = double("amount")
    val unitType = varchar("unittype", 255)
}

