package service

import models.dto.AllergenEntry
import repository.RecipeAllergensRepositoryImpl

class RecipeAllergenService(val recipeAllergensRepository: RecipeAllergensRepositoryImpl) {
    suspend fun getAllergensByRecipeId(recipeId : Int, allergenService : AllergenService): List<AllergenEntry> {
        val recipeAllergens =  recipeAllergensRepository.findAllByRecipeId(recipeId)
        val allergens = mutableListOf<AllergenEntry>()
        for (allergenEntry in recipeAllergens) {
            val allergen = allergenService.getAllergenById(allergenEntry.allergenId)
            if (allergen != null) {
                allergens.add(allergen)
            }
        }
        return allergens
    }
}