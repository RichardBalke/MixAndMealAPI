package models.tables


import org.jetbrains.exposed.sql.Table

object UserFridge : Table("user_fridge") {
    val userId = varchar("user_id", 255).references(Users.email)
    val ingredientName = varchar("ingredient_name", 255).references(Ingredients.name)

    override val primaryKey = PrimaryKey(userId, ingredientName)
}