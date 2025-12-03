package models.tables


import org.jetbrains.exposed.sql.Table

object UserFavourite : Table("user_favourites") {
    val userId = varchar("user_id", 255).references(User.email)
    val recipeId = integer("recipe_id").references(Recipe.id)

    override val primaryKey = PrimaryKey(userId, recipeId)
}