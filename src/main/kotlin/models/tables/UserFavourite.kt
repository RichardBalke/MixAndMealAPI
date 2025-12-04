package models.tables


import org.jetbrains.exposed.sql.Table

object UserFavourites : Table("user_favourites") {
    val userId = varchar("user_id", 255).references(Users.email)
    val recipeId = integer("recipe_id").references(Recipes.id)

    override val primaryKey = PrimaryKey(userId, recipeId)
}