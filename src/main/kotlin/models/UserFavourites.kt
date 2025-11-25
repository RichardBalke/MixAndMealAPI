package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import api.models.Users
import api.models.Recipes

@Serializable
data class UserFavouriteEntry(
    val userId: String,
    val recipeId: Int
)

object UserFavourites : Table("user_favourites") {
    val userId = varchar("user_id", 255).references(Users.email)
    val recipeId = integer("recipe_id").references(Recipes.id)

    override val primaryKey = PrimaryKey(userId, recipeId)
}