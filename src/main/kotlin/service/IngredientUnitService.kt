package service

import api.repository.IngredientUnitRepositoryImpl
import models.dto.IngredientUnitEntry

class IngredientUnitService(private val ingredientUnitRepository: IngredientUnitRepositoryImpl) {
    suspend fun getIngredientsByRecipeId(id : Int): List<IngredientUnitEntry>{
        return ingredientUnitRepository.findAllByRecipeId(id)
    }

    suspend fun addIngredientUnit(
        ingredientUnit: IngredientUnitEntry
    ){
        ingredientUnitRepository.create(ingredientUnit)
    }
}