package models

import org.jetbrains.exposed.sql.Table

data class IngredientAllergen(
    val ingredientName:String,
    val allergenId:String
)

object IngredientAllergens : Table(){
    val ingredientName = varchar("ingredient_name", 255)
    val allergenId = varchar("allergen_id", 255)
}
