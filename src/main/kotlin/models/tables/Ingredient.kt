package models.tables

import org.jetbrains.exposed.sql.Table

object Ingredient : Table(){
    val name = varchar("name", 255).uniqueIndex()
    val description = text("description")
}