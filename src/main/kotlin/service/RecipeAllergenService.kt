package service

import models.dto.AllergenEntry
import models.dto.DietEntry
import models.dto.RecipeDietEntry
import org.jetbrains.exposed.sql.transactions.transaction
import repository.RecipeAllergensRepository

class RecipeAllergenService(val recipeAllergensRepository: RecipeAllergensRepository) {
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