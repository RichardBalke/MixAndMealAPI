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
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.java.KoinJavaComponent.inject
import repository.AllergensRepository
import repository.DietsRepository
import repository.RecipeAllergensRepositoryImpl
import requests.RecipeSearchRequest
import kotlin.String

class RecipeService(
    private val recipeRepository : RecipesRepository,
    private val dietsRepository: DietsRepository,
    private val allergensRepository: AllergensRepository
) {

    suspend fun createUploadedRecipe(
        uploadedRecipe: RecipeUploadRequest,
        recipeImagesService: RecipeImagesService,
        recipeDietsService: RecipeDietsService,
        recipeAllergenService: RecipeAllergenService,
        ingredientUnitService: IngredientUnitService
    ): Int {
        return newSuspendedTransaction {
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
            val newRecipe = recipeRepository.create(recipe)  // Changed from addRecipes

            // All service calls NOW work because they're inside transaction block
            uploadedRecipe.images.forEach { image ->
                recipeImagesService.addImage(newRecipe.id, image.imageUrl)
            }

            uploadedRecipe.diets.forEach { dietRequest ->
                dietsRepository.findByDisplayName(dietRequest.displayName)?.let { diet ->
                    recipeDietsService.addRecipeDiet(RecipeDietEntry(newRecipe.id, diet.id))
                }
            }

            uploadedRecipe.allergens.forEach { allergenRequest ->
                allergensRepository.findByDisplayName(allergenRequest.displayName)?.let { allergen ->
                    recipeAllergenService.addRecipeAllergen(RecipeAllergenEntry(newRecipe.id, allergen.id))
                }
            }

            uploadedRecipe.ingredients.forEach { ingredient ->
                ingredientUnitService.addIngredientUnit(
                    IngredientUnitEntry(newRecipe.id, ingredient.ingredientName, ingredient.amount, ingredient.unitType)
                )
            }
            newRecipe.id
        }
    }


    fun formatCookingTime(minutes: Int): String {
        val hours = minutes / 60
        val minutes = minutes % 60
        return "${hours}h ${minutes}m"
    }

    suspend fun searchRecipes(
        recipeSearchRequest: RecipeSearchRequest,
        recipeImagesService: RecipeImagesService
    ): List<RecipeCardResponse> {
        val rawRecipes = recipeRepository.searchRecipesRaw()
        rawRecipes.filter {
            (recipeSearchRequest.partialTitle == null || it.title.contains(recipeSearchRequest.partialTitle)) &&
            (recipeSearchRequest.difficulty == null || it.difficulty == recipeSearchRequest.difficulty.uppercase()) &&
            (recipeSearchRequest.mealType == null || recipeSearchRequest.mealType == it.mealType) &&
            (recipeSearchRequest.kitchenStyle == null || it.kitchenStyle == recipeSearchRequest.kitchenStyle) &&
            (recipeSearchRequest.maxCookingTime == null || it.cookingTime <= recipeSearchRequest.maxCookingTime) &&
            (recipeSearchRequest.diets.isEmpty() || recipeSearchRequest.diets.contains(it.dietId)) &&
            (recipeSearchRequest.allergens.isEmpty() || recipeSearchRequest.allergens.contains(it.allergenId)) &&
            (recipeSearchRequest.ingredients.isEmpty() || recipeSearchRequest.ingredients.contains(it.ingredientName))
        }.toSet()

        val recipes = recipeRepository.findRecipesFromRawRecipes(rawRecipes)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun getAllRecipes(): List<RecipeCardResponse> {
        return recipeRepository.findAllRecipesAsRecipeCards()
    }

    suspend fun addRecipes(recipe: RecipeEntry): RecipeEntry {
        return recipeRepository.create(recipe)
    }

    suspend fun getRecipe(id: Int): RecipeEntry? {
        return recipeRepository.findByRecipeId(id)
    }

    suspend fun deleteRecipe(id: Int): Boolean {
        return recipeRepository.delete(id)
    }

    suspend fun findByTitle(title: String): List<RecipeEntry> {
        return recipeRepository.findByTitle(title)
    }

    suspend fun findByDifficulty(difficulty: String): List<RecipeEntry> {
        return recipeRepository.findByDifficulty(difficulty)
    }

    suspend fun findByMealType(mealType: String): List<RecipeEntry> {
        return recipeRepository.findByMealType(mealType)
    }

    suspend fun findByDiets(diets: String): List<RecipeEntry> {
        return recipeRepository.findByDiets(diets)
    }

    suspend fun findByKitchenStyle(kitchenStyle: String): List<RecipeEntry> {
        return recipeRepository.findByKitchenStyle(kitchenStyle)
    }

    suspend fun findPopularRecipes(limit: Int, recipeImagesService: RecipeImagesService): List<RecipeCardResponse> {
        val recipes = recipeRepository.findPopularRecipes(limit)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun findRecipesByDifficulty(
        limit: Int,
        difficulty: String,
        recipeImagesService: RecipeImagesService
    ): List<RecipeCardResponse> {
        val recipes = recipeRepository.findRecipeCardsByDifficulty(limit, difficulty)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun findQuickRecipes(limit: Int, recipeImagesService: RecipeImagesService): List<RecipeCardResponse> {
        val recipes = recipeRepository.findQuickRecipes(limit)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }

    suspend fun findFavouriteRecipes(
        recipeIds: List<UserFavouritesEntry>,
        recipeImagesService: RecipeImagesService
    ): List<RecipeCardResponse> {
        val recipes = recipeRepository.findFavoriteRecipes(recipeIds)
        for (recipe in recipes) {
            recipe.imageUrl.addAll(recipeImagesService.getImagesForRecipe(recipe.recipeId))
        }
        return recipes
    }
}