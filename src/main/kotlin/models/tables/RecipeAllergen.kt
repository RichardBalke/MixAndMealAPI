package models.tables

import org.jetbrains.exposed.sql.Table

object RecipeAllergen : Table(){
    val recipeId = integer("recipe_id").references(Recipe.id)
    val allergenId = integer("allergen_id").references(Allergen.id)

    override val primaryKey = PrimaryKey(recipeId, allergenId)
}