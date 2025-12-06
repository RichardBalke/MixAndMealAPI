package service


import models.dto.DietEntry
import repository.RecipeDietsRepository

class RecipeDietsService(private val recipeDietsRepository: RecipeDietsRepository) {
    suspend fun getDietsbyRecipeId(recipeID: Int, dietsService: DietsService): List<DietEntry> {
        val recipeDiets =  recipeDietsRepository.findAllByRecipeId(recipeID)
        val diets = mutableListOf<DietEntry>()
        for (dietEntry in recipeDiets) {
            val diet = dietsService.getDietById(dietEntry.dietId)
            if (diet != null) {
                diets.add(diet)
            }
        }
        return diets
    }
}