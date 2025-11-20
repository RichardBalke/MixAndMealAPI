package service

import api.models.Diets
import api.models.KitchenStyle
import api.repository.RecipesRepository
import api.repository.FakeRecipeRepository.currentID
import api.repository.FakeRecipeRepository.recipes
import api.models.Recipes


class RecipeService {

    fun formatCookingTime(minutes: Int): String {
        val hours = minutes / 60
        val minutes = minutes % 60
        return "${hours}h ${minutes}m"
    }

//    Ingredienten en recepten matching functie. Matcht op minsten 1 ingredient aanwezig. sortering op beste matches
//
//    suspend fun findBestMatchesByIngredients(selectedIngredients: List<String>): List<Pair<Recipe, Double>> {
//        return recipes
//            .map { recipe ->
//                val totalIngredients = recipe.ingredients.size
//                val matchedCount = recipe.ingredients.count { selectedIngredients.contains(it.ingredient.name) }
//                val matchPercentage = if (totalIngredients > 0) matchedCount.toDouble() / totalIngredients else 0.0
//                recipe to matchPercentage
//            }
//            .filter { it.second > 0 }
//            .sortedByDescending { it.second }
//    }
}
