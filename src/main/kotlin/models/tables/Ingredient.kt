package models.tables

import org.jetbrains.exposed.sql.Table

object Ingredients : Table(){
    val name = varchar("name", 255).uniqueIndex()
    val description = text("description")
}