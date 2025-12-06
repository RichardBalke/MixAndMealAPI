package models.tables


import org.jetbrains.exposed.sql.Table

object IngredientUnits : Table("ingredient_units") {
    val recipeId = integer("recipe_id").references(Recipes.id)
    val ingredientName = varchar("ingredient_name", 255).references(Ingredients.name)
    val amount = double("amount")
    val unitType = varchar("unittype", 255)

    override val primaryKey = PrimaryKey(recipeId, ingredientName)
}