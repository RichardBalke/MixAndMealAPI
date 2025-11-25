package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class UserFridgeEntry (
    val userId: String,
    val ingredientName: String
)

object UserFridge : Table("user_fridge") {
    val userId = varchar("user_id", 255).references(Users.email)
    val ingredientName = varchar("ingredient_name", 255).references(Ingredients.name)

    override val primaryKey = PrimaryKey(userId, ingredientName)
}

