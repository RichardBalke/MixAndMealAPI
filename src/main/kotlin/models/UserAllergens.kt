package api.models

import api.models.Allergens
import api.models.Users
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class UserAllergenEntry(
    val userId: String,
    val allergenId: Int
)

object UserAllergens : Table("user_allergens") {
    val userId = varchar("user_id", 255).references(Users.email)
    val allergenId = integer("allergen_id").references(Allergens.id)

    override val primaryKey = PrimaryKey(userId, allergenId)
}