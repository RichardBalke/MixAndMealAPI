package models.tables

import org.jetbrains.exposed.sql.Table

object User: Table() {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val role = varchar("role", 255)
}