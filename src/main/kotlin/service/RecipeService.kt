package service

import api.repository.RecipesRepository
import api.repository.RecipesRepositoryImpl
import api.responses.RecipeCardResponse
import models.dto.RecipeEntry
import models.tables.Recipes
import org.koin.java.KoinJavaComponent.inject
import repository.RecipeAllergensRepositoryImpl
import requests.RecipeSearchRequest

class RecipeService(private val recipeRepository : RecipesRepository) {
    fun formatCookingTime(minutes: Int): String {
        val hours = minutes / 60
        val minutes = minutes % 60
        return "${hours}h ${minutes}m"
    }

    suspend fun searchRecipes(request: RecipeSearchRequest): List<RecipeEntry> {
        val recipes = mutableSetOf<RecipeEntry>()
        if(request.partialTitle.isNotBlank()){
            recipes.addAll(findByTitle(request.partialTitle))
        }
        if(request.difficulty.isNotBlank()){
            recipes.addAll(findByDifficulty(request.difficulty))
        }
        if(request.mealType.isNotBlank()){
            recipes.addAll(findByMealType(request.mealType))
        }
        if(request.kitchenStyle.isNotBlank()){
            recipes.addAll(findByKitchenStyle(request.kitchenStyle))
        }
//        if(request.maxCookingTime != 0){
//            TODO("create function to get recipes with les then max cooking time")
//        }
        if(request.diets.isNotEmpty()){
            for(diet in request.diets){
                recipes.addAll(findByDiets(diet))
            }
        }
        if(request.allergens.isNotEmpty()){
            for(allergen in request.allergens){
                val recipeAllergenService = RecipeAllergenService(RecipeAllergensRepositoryImpl())
                recipes.addAll(recipeAllergenService.getRecipesByAllergenId(allergen, this))
            }
        }
//        if(request.ingredients.isNotEmpty()){
//            TODO("create function to get recipes with ingredients")
//        }


        return recipes.toList()
    }

    suspend fun getAllRecipes(): List<RecipeEntry> {
        return recipeRepository.findAll()
    }

    suspend fun addRecipes(recipe: RecipeEntry) {
        recipeRepository.create(recipe)
    }

    suspend fun getRecipe(id: Int): RecipeEntry? {
        return recipeRepository.findByRecipeId(id)
    }

    suspend fun deleteRecipe(id: Int) : Boolean {
        return recipeRepository.delete(id)
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

    suspend fun findPopularRecipes(limit: Int, recipeImagesService: RecipeImagesService): List<RecipeCardResponse> {
        val recipes = recipeRepository.findPopularRecipes(limit)
        for(recipe in recipes){
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

}
