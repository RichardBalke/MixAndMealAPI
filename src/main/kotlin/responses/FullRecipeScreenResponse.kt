package responses

import kotlinx.serialization.Serializable
import models.dto.AllergenEntry
import models.dto.DietEntry
import models.dto.IngredientUnitEntry
import models.enums.Difficulty
import models.enums.KitchenStyle
import models.enums.MealType

@Serializable
data class FullRecipeScreenResponse(
    val id: Int,
    val title: String,
    val description: String,
    val instructions: String,
    val prepTime: Int,
    val cookingTime: Int,
    val difficulty: Difficulty,
    val image: ByteArray?,
    val mealType: MealType,
    val kitchenStyle: KitchenStyle,
    val diets: List<DietEntry>,
    val allergens: List<AllergenEntry>,
    val ingredients: List<IngredientUnitEntry>
)
