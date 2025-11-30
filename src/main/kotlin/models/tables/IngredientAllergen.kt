package models.tables

import org.jetbrains.exposed.sql.Table

object IngredientAllergen : Table(){
    val ingredientName = varchar("ingredient_name", 255).references(Ingredient.name)
    val allergenId = integer("allergen_id").references(Allergen.id)

    override val primaryKey = PrimaryKey(ingredientName, allergenId)
}