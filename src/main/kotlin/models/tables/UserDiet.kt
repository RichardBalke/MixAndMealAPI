package models.tables

import org.jetbrains.exposed.sql.Table

object UserDiets : Table("user_diets") {
    val userId = varchar("user_id", 255).references(User.email)
    val dietId = integer("diet_id").references(Diet.id)

    override val primaryKey = PrimaryKey(userId, dietId)
}