package models.tables

import org.jetbrains.exposed.sql.Table

object RecipeDiet : Table() {
    val recipeId = integer("recipe_id").references(Recipe.id)
    val dietId = integer("diet_id").references(Diet.id)

    override val primaryKey = PrimaryKey(recipeId, dietId)
}