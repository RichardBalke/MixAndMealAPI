package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Ingredient(
    val name: String,
    val description: String
)

object Ingredients : Table(){
    val name = varchar("name", 255).uniqueIndex()
    val description = text("description")
}