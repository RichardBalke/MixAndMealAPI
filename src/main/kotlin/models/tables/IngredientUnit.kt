package models.tables


import org.jetbrains.exposed.sql.Table

object IngredientUnit : Table() {
    val recipeId = integer("recipe_id").references(Recipe.id)
    val ingredientName = varchar("ingredient_name", 255).references(Ingredient.name)
    val amount = double("amount")
    val unitType = varchar("unittype", 255)

    override val primaryKey = PrimaryKey(recipeId, ingredientName)
}