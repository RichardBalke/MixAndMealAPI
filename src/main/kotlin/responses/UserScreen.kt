package responses

import api.models.Allergen
import api.models.Diet
import api.models.Ingredient
import api.models.Recipe
import kotlinx.serialization.Serializable

@Serializable
class UserScreen (
    val username: String,
    // Recipe should not be all info of a recipe so this should be another response class instead of recipe
    val favorites : Set<Recipe>,
    val allergens : Set<Allergen>,
    val diets : Set<Diet>,
    val fridge : Set<Ingredient>
)