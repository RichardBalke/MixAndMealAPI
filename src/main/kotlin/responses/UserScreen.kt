package responses

import kotlinx.serialization.Serializable
import models.dto.AllergenEntry
import models.dto.DietEntry
import models.dto.IngredientEntry
import models.dto.RecipeEntry


@Serializable
class UserScreen (
    val username: String,
    // Recipe should not be all info of a recipe so this should be another response class instead of recipe
    val favorites : Set<RecipeEntry>,
    val allergens : Set<AllergenEntry>,
    val diets : Set<DietEntry>,
    val fridge : Set<IngredientEntry>
)