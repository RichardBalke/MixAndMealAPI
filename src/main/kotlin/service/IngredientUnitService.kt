package service

import api.repository.IngredientUnitRepository
import models.dto.IngredientUnitEntry

class IngredientUnitService(private val ingredientUnitRepository: IngredientUnitRepository) {
    suspend fun getIngredientsByRecipeId(id : Int): List<IngredientUnitEntry>{
        return ingredientUnitRepository.findAllByRecipeId(id)
    }
}