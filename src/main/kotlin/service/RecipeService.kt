package service

import api.repository.RecipesRepository
import models.dto.RecipeEntry

class RecipeService(private val recipeRepository: RecipesRepository) {

    fun formatCookingTime(minutes: Int): String {
        val hours = minutes / 60
        val minutes = minutes % 60
        return "${hours}h ${minutes}m"
    }

    suspend fun findByTitle(title: String): List<RecipeEntry>{
        return recipeRepository.findByTitle(title)
    }
    suspend fun findByDifficulty(difficulty: String): List<RecipeEntry>{
        return recipeRepository.findByDifficulty(difficulty)
    }
    suspend fun findByMealType(mealType: String): List<RecipeEntry>{
        return recipeRepository.findByMealType(mealType)
    }
    suspend fun findByDiets(diets: String): List<RecipeEntry>{
        return recipeRepository.findByDiets(diets)
    }
    suspend fun findByKitchenStyle(kitchenStyle: String): List<RecipeEntry>{
        return recipeRepository.findByKitchenStyle(kitchenStyle)
    }
}
