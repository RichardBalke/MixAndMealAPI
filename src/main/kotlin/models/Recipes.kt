package api.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Recipe(
    val id: Int,
    val title: String,
    val description: String,
    val prepTime: Int,
    val cookingTime: Int,
    val difficulty: Difficulty,
    val image: String,
    val mealType: MealType,
    val kitchenStyle: KitchenStyle
//    val diets: List<Diets>,
//    val ingredients: List<IngredientUnits>

)


object Recipes : Table() {
    val id = integer("id").autoIncrement().uniqueIndex()
    val title = varchar("title", 255)
    val description = varchar("description", 255)
    val prepTime = integer("prepTime")
    val cookingTime = integer("cookingTime")
    val difficulty = varchar("difficulty", 255)
    val image = varchar("image", 255)
    val mealType = varchar("mealtype", 255)
    val kitchenStyle = varchar("kitchen_style", 255)
}

