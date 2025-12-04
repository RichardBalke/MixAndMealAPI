package models.tables

import org.jetbrains.exposed.sql.Table

object RecipeAllergens : Table(){
    val recipeId = integer("recipe_id").references(Recipes.id)
    val allergenId = integer("allergen_id").references(Allergens.id)

    override val primaryKey = PrimaryKey(recipeId, allergenId)
}