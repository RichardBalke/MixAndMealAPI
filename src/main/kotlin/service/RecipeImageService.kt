package service

import models.dto.RecipeImageEntry
import repository.RecipeImageRepository

class RecipeImagesService(private val recipeImageRepository: RecipeImageRepository) {

    suspend fun getImagesForRecipe(recipeId: Int): List<RecipeImageEntry> {
        return recipeImageRepository.getImagesForRecipe(recipeId)
    }

    suspend fun addImage(recipeId: Int, imageUrl: String): RecipeImageEntry {
        return recipeImageRepository.addImage(recipeId, imageUrl)
    }

    suspend fun deleteImage(imageId: Int) {
        recipeImageRepository.deleteImage(imageId)
    }
}