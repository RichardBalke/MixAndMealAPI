package models.tables

import org.jetbrains.exposed.sql.Table

object Recipes : Table() {
    val id = integer("recipe_id").autoIncrement().uniqueIndex()
    val title = varchar("title", 255)
    val description = varchar("description", 255)
    val prepTime = integer("preptime")
    val cookingTime = integer("cookingtime")
    val difficulty = varchar("difficulty", 255)
    val image = varchar("image", 255)
    val mealType = varchar("mealtype", 255)
    val kitchenStyle = varchar("kitchenstyle", 255)
}