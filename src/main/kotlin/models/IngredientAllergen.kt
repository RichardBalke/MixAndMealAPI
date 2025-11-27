package models

import api.models.Allergens
import api.models.Ingredients
import org.jetbrains.exposed.sql.Table

data class IngredientAllergen(
    val ingredientName:String,
    val allergenId : Int
)

object IngredientAllergens : Table(){
    val ingredientName = varchar("ingredient_name", 255).references(Ingredients.name)
    val allergenId = integer("allergen_id").references(Allergens.id)

    override val primaryKey = PrimaryKey(ingredientName, allergenId)
}
