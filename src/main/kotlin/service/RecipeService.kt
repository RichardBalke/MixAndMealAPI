package service

import api.repository.RecipesRepository
import api.repository.RecipesRepositoryImpl
import api.requests.RecipeUploadRequest
import api.responses.RecipeCardResponse
import models.dto.IngredientUnitEntry
import models.dto.RecipeAllergenEntry
import models.dto.RecipeDietEntry
import models.dto.RecipeEntry
import models.dto.UserFavouritesEntry
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

//    suspend fun searchRecipes() {
//        val rawRecipes = recipeRepository.searchRecipesRaw()
//        rawRecipes.filter{
//
//        }
//    }

    suspend fun getAllRecipes(): List<RecipeCardResponse> {
        return recipeRepository.findAllRecipesAsRecipeCards()
    }

    suspend fun addRecipes(recipe: RecipeEntry): RecipeEntry {
        return recipeRepository.create(recipe)
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

    suspend fun findRecipesByDifficulty(limit: Int, difficulty: String, recipeImagesService: RecipeImagesService): List<RecipeCardResponse> {
        val recipes = recipeRepository.findRecipeCardsByDifficulty(limit, difficulty)
        for(recipe in recipes){
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }
    suspend fun findQuickRecipes(limit: Int, recipeImagesService: RecipeImagesService): List<RecipeCardResponse> {
        val recipes = recipeRepository.findQuickRecipes(limit)
        for(recipe in recipes){
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun findFavouriteRecipes(recipeIds: List<UserFavouritesEntry>, recipeImagesService: RecipeImagesService) : List<RecipeCardResponse>{
        val recipes = recipeRepository.findFavoriteRecipes(recipeIds)
        for(recipe in recipes){
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun createUploadedRecipe(
        uploadedRecipe: RecipeUploadRequest,
        recipeImagesService: RecipeImagesService,
        recipeDietsService: RecipeDietsService,
        recipeAllergenService: RecipeAllergenService,
        ingredientUnitService: IngredientUnitService,

    ) {
        val recipe = RecipeEntry(
            0,
            uploadedRecipe.title,
            uploadedRecipe.description,
            uploadedRecipe.instructions,
            uploadedRecipe.prepTime,
            uploadedRecipe.cookingTime,
            uploadedRecipe.difficulty,
            uploadedRecipe.mealType,
            uploadedRecipe.kitchenStyle,
            0
        )

        val recipeImage = uploadedRecipe.images
        val recipeDiets = uploadedRecipe.diets
        val recipeAllergens = uploadedRecipe.allergens
        val recipeIngredients = uploadedRecipe.ingredients

        val newRecipe = addRecipes(recipe)


        for (image in recipeImage) {
            recipeImagesService.addImage(
                newRecipe.id,
                image.imageUrl
            )
        }

        for (diets in recipeDiets) {
            val newRecipeDiet = RecipeDietEntry(newRecipe.id, diets.id)
            recipeDietsService.addRecipeDiet(newRecipeDiet)
        }

        for (allergens in recipeAllergens) {
            val newRecipeAllergen = RecipeAllergenEntry(newRecipe.id, allergens.id)
            recipeAllergenService.addRecipeAllergen(newRecipeAllergen)
        }

        for (ingredient in recipeIngredients) {
            val newIngredientUnit = IngredientUnitEntry(newRecipe.id, ingredient.ingredientName, ingredient.amount,ingredient.unitType)
            ingredientUnitService.addIngredientUnit(newIngredientUnit)
        }
    }

}
