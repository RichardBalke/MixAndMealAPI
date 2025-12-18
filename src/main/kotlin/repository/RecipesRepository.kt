package api.repository

import api.responses.RecipeCardResponse
import models.enums.Difficulty
import models.enums.KitchenStyle
import models.enums.MealType
import models.dto.RecipeEntry
import models.dto.RecipeImageEntry
import models.tables.Recipes
import models.tables.RecipeDiets
import models.tables.Diets
import models.tables.RecipeImages
import org.h2.api.H2Type.row
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface RecipesRepository : CrudRepository<RecipeEntry, Int>{
    suspend fun findByTitle(title: String): List<RecipeEntry>
    suspend fun findByDifficulty(difficulty: String): List<RecipeEntry>
    suspend fun findByMealType(mealType: String): List<RecipeEntry>
    suspend fun findByDiets(diets: String): List<RecipeEntry>
    suspend fun findByKitchenStyle(kitchenStyle: String): List<RecipeEntry>
    suspend fun findByRecipeId(recipeId: Int): RecipeEntry?
    suspend fun findPopularRecipes(limit: Int): List<RecipeCardResponse>
    suspend fun findRecipeCardsByDifficulty(limit: Int, difficulty: String): List<RecipeCardResponse>
    suspend fun findQuickRecipes(limit: Int): List<RecipeCardResponse>

//    suspend fun updateImage(recipeID: Long, imageUrl: String): Boolean
//    suspend fun findByFavourites(favourites : Favourites): List<Recipes>
}


class RecipesRepositoryImpl : RecipesRepository, CrudImplementation<RecipeEntry, Int>(
    table = Recipes,
    toEntity = { row ->
        val difficultyString = row[Recipes.difficulty]
        val difficultyEnum = Difficulty.valueOf(difficultyString)
        val mealTypeString = row[Recipes.mealType]
        val mealTypeEnum = MealType.valueOf(mealTypeString)
        val kitchenStyleString = row[Recipes.kitchenStyle]
        val kitchenStyleEnum = KitchenStyle.valueOf(kitchenStyleString)
        RecipeEntry(row[Recipes.id],
            row[Recipes.title],
            row[Recipes.description],
            row[Recipes.instructions],
            row[Recipes.prepTime],
            row[Recipes.cookingTime],
            difficultyEnum,
            mealTypeEnum,
            kitchenStyleEnum,
            row[Recipes.favoritesCount]) },
    idColumns = listOf(Recipes.id),
    idExtractor =  { entry -> listOf(entry) },
    entityMapper = { stmt, recipe ->
        stmt[Recipes.id] = recipe.id
        stmt[Recipes.title] = recipe.title
        stmt[Recipes.description] = recipe.description
        stmt[Recipes.instructions] = recipe.instructions
        stmt[Recipes.prepTime] = recipe.prepTime
        stmt[Recipes.cookingTime] = recipe.cookingTime
        stmt[Recipes.difficulty] = recipe.difficulty.name
        stmt[Recipes.mealType] = recipe.mealType.name
        stmt[Recipes.kitchenStyle] = recipe.kitchenStyle.name
        stmt[Recipes.favoritesCount] = recipe.favoritesCount
    }){

    override suspend fun findByTitle(title: String): List<RecipeEntry>  = transaction {
        Recipes.selectAll()
            .where { Recipes.title like title }
            .mapNotNull(toEntity)
            .toList()
    }

    override suspend fun findByDifficulty(difficulty: String): List<RecipeEntry> = transaction{
        Recipes.selectAll()
            .where(Recipes.difficulty eq difficulty)
            .mapNotNull(toEntity)
            .toList()
    }

    override suspend fun findByMealType(mealType: String): List<RecipeEntry> = transaction {
        Recipes.selectAll()
            .where(Recipes.mealType eq mealType)
            .mapNotNull(toEntity)
            .toList()
    }


    // Deze functie moet goed getest worden. Als dit werkt kunnen we op deze manier ook andere queries doen!!!
    // !!!
    override suspend fun findByDiets(diets: String): List<RecipeEntry> = transaction {
        (Recipes innerJoin RecipeDiets innerJoin Diets)
            .selectAll()
            .where(Diets.displayName eq diets)
            .mapNotNull(toEntity)
            .toList()
    }

    override suspend fun findByKitchenStyle(kitchenStyle: String): List<RecipeEntry> = transaction {
        Recipes.selectAll()
            .where(Recipes.kitchenStyle eq kitchenStyle)
            .mapNotNull(toEntity)
            .toList()
    }

    override suspend fun findByRecipeId(recipeId: Int): RecipeEntry? = transaction {
        table.selectAll()
            .where { Recipes.id eq recipeId }
            .mapNotNull(toEntity)
            .firstOrNull()
    }

    override suspend fun findPopularRecipes(limit: Int): List<RecipeCardResponse> = transaction {
        table.select(Recipes.id, Recipes.title, Recipes.description, Recipes.cookingTime, Recipes.favoritesCount)
            .orderBy(Recipes.favoritesCount, SortOrder.DESC)
            .limit(limit)
            .map {RecipeCardResponse(
                recipeId = it[Recipes.id],
                it[Recipes.title],
                it[Recipes.description],
                it[Recipes.cookingTime],
                mutableListOf()
            )
            }
            .toList()
    }

    override suspend fun findRecipeCardsByDifficulty(limit: Int, difficulty: String): List<RecipeCardResponse> = transaction {
        table.select(Recipes.id, Recipes.title, Recipes.description, Recipes.cookingTime)
            .where(Recipes.difficulty eq difficulty)
            .limit(limit)
            .map{RecipeCardResponse(
                recipeId = it[Recipes.id],
                it[Recipes.title],
                it[Recipes.description],
                it[Recipes.cookingTime],
                mutableListOf())
            }
            .toList()
    }

    override suspend fun findQuickRecipes(limit: Int): List<RecipeCardResponse> = transaction {
        table.select(Recipes.id, Recipes.title, Recipes.description, Recipes.cookingTime)
            .where(Recipes.cookingTime.lessEq(30))
            .orderBy(Recipes.cookingTime)
            .limit(limit)
            .map{RecipeCardResponse(
                recipeId = it[Recipes.id],
                it[Recipes.title],
                it[Recipes.description],
                it[Recipes.cookingTime],
                mutableListOf())
            }
            .toList()
    }

}
