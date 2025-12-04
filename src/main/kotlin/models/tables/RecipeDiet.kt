package models.tables

import org.jetbrains.exposed.sql.Table

object RecipeDiets : Table() {
    val recipeId = integer("recipe_id").references(Recipes.id)
    val dietId = integer("diet_id").references(Diets.id)

    override val primaryKey = PrimaryKey(recipeId, dietId)
}