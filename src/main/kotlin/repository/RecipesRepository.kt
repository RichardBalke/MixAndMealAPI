package api.repository

import models.enums.Difficulty
import models.enums.KitchenStyle
import models.enums.MealType
import api.models.Recipe
import api.models.Recipes

interface RecipesRepository : CrudRepository<Recipe, Int> {
    suspend fun findByTitle(title: String): List<Recipe>
    suspend fun findByDifficulty(difficulty: String?): List<Recipe>
    suspend fun findByMealType(mealType: String?): List<Recipe>
    suspend fun findByDiets(diets: String): List<Recipe>
    suspend fun findByKitchenStyle(kitchenStyle: String): List<Recipe>

//    suspend fun updateImage(recipeID: Long, imageUrl: String): Boolean
//    suspend fun findByFavourites(favourites : Favourites): List<Recipes>
}


class RecipesRepositoryImpl : CrudImplementation<Recipe, Int>(
    table = Recipes,
    toEntity = { row ->
        val difficultyString = row[Recipes.difficulty]
        val difficultyEnum = Difficulty.valueOf(difficultyString)
        val mealTypeString = row[Recipes.mealType]
        val mealTypeEnum = MealType.valueOf(mealTypeString)
        val kitchenStyleString = row[Recipes.kitchenStyle]
        val kitchenStyleEnum = KitchenStyle.valueOf(kitchenStyleString)
        Recipe(row[Recipes.id],
            row[Recipes.title],
            row[Recipes.description],
            row[Recipes.prepTime],
            row[Recipes.cookingTime],
            difficultyEnum,
            row[Recipes.image],
            mealTypeEnum,
            kitchenStyleEnum) },
    idColumns = listOf(Recipes.id),
    idExtractor =  { listOf(Int) },
    entityMapper = { stmt, recipe ->
        stmt[Recipes.id] = recipe.id
        stmt[Recipes.title] = recipe.title
        stmt[Recipes.description] = recipe.description
        stmt[Recipes.prepTime] = recipe.prepTime
        stmt[Recipes.cookingTime] = recipe.cookingTime
        stmt[Recipes.difficulty] = recipe.difficulty.name
        stmt[Recipes.image] = recipe.image
        stmt[Recipes.mealType] = recipe.mealType.name
        stmt[Recipes.kitchenStyle] = recipe.kitchenStyle.name
    }), RecipesRepository {

    override suspend fun findByTitle(title: String): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findByDifficulty(difficulty: String?): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findByMealType(mealType: String?): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findByDiets(diets: String): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun findByKitchenStyle(kitchenStyle: String): List<Recipe> {
        TODO("Not yet implemented")
    }

}
