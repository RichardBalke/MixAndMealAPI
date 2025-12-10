package models.tables

import org.jetbrains.exposed.sql.Table

object Diets : Table() {
    val id = integer(name = "diet_id").uniqueIndex().autoIncrement()
    val displayName = varchar("displayname", 255)
    val description = text("description")
}