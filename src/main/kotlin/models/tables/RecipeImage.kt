package models.tables

import org.jetbrains.exposed.sql.Table
import java.util.UUID

object RecipeImages : Table("recipe_images") {
    val id = integer("recipe_image_id").autoIncrement().uniqueIndex()
    val recipeId = integer("recipe_id").references(Recipes.id)
    val imageUrl = varchar("image_url", 255)

    override val primaryKey = PrimaryKey(id)
}