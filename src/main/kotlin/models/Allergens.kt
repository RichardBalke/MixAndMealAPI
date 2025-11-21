package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Allergen(
    val id: Int,
    val name: String,
    val displayName: String,
    val description: String
)

object Allergens : Table() {
    val id = integer(name = "id").autoIncrement().uniqueIndex()
    val name = varchar("name", 255)
    val displayName = varchar("displayname", 255)
    val description = text("description")
}

