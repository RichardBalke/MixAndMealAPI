package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table


@Serializable
data class UserDietEntry (
    val userId: String,
    val dietId: Int,
)

object UserDiets : Table("user_diets") {
    val userId = varchar("user_id", 255).references(Users.email)
    val dietId = integer("diet_id").references(Diets.id)

    override val primaryKey = PrimaryKey(userId, dietId)
}