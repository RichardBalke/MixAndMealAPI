package models.tables

import org.jetbrains.exposed.sql.Table

object Allergens : Table() {
    val id = integer(name = "id").autoIncrement().uniqueIndex()
    val name = varchar("name", 255)
    val displayName = varchar("displayname", 255)
    val description = text("description")
}