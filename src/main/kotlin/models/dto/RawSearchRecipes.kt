package api.models.dto


data class RawSearchRecipes(
    val recipeId : Int,
    val title: String,
    val description: String,
    val instructions: String,
    val prepTime: Int,
    val cookingTime: Int,
    val difficulty: String,
    val mealType: String,
    val kitchenStyle: String,
    val dietId: Int,
    val allergenId: Int,
    val ingredientName: String
)
