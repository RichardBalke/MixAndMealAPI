package models.tables


import org.jetbrains.exposed.sql.Table

object UserAllergens : Table("user_allergens") {
    val userId = varchar("user_id", 255).references(User.email)
    val allergenId = integer("allergen_id").references(Allergen.id)

    override val primaryKey = PrimaryKey(userId, allergenId)
}