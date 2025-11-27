package models

import api.models.Diets
import api.models.Recipes
import org.jetbrains.exposed.sql.Table

data class RecipeDiet(
    val recipeId: Int,
    val dietId: Int
)

object RecipeDiets : Table() {
    val recipeId = integer("recipe_id").references(Recipes.id)
    val dietId = integer("diet_id").references(Diets.id)

    override val primaryKey = PrimaryKey(recipeId, dietId)
}