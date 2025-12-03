package api.repository

import models.enums.Difficulty
import models.enums.KitchenStyle
import models.enums.MealType
import models.dto.RecipeEntry
import models.tables.Recipe
import models.tables.RecipeDiet
import models.tables.Diet
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface RecipesRepository : CrudRepository<RecipeEntry, Int> {
    suspend fun findByTitle(title: String): List<RecipeEntry>
    suspend fun findByDifficulty(difficulty: String): List<RecipeEntry>
    suspend fun findByMealType(mealType: String): List<RecipeEntry>
    suspend fun findByDiets(diets: String): List<RecipeEntry>
    suspend fun findByKitchenStyle(kitchenStyle: String): List<RecipeEntry>

//    suspend fun updateImage(recipeID: Long, imageUrl: String): Boolean
//    suspend fun findByFavourites(favourites : Favourites): List<Recipes>
}


class RecipesRepositoryImpl : CrudImplementation<RecipeEntry, Int>(
    table = Recipe,
    toEntity = { row ->
        val difficultyString = row[Recipe.difficulty]
        val difficultyEnum = Difficulty.valueOf(difficultyString)
        val mealTypeString = row[Recipe.mealType]
        val mealTypeEnum = MealType.valueOf(mealTypeString)
        val kitchenStyleString = row[Recipe.kitchenStyle]
        val kitchenStyleEnum = KitchenStyle.valueOf(kitchenStyleString)
        RecipeEntry(row[Recipe.id],
            row[Recipe.title],
            row[Recipe.description],
            row[Recipe.prepTime],
            row[Recipe.cookingTime],
            difficultyEnum,
            row[Recipe.image],
            mealTypeEnum,
            kitchenStyleEnum) },
    idColumns = listOf(Recipe.id),
    idExtractor =  { listOf(Int) },
    entityMapper = { stmt, recipe ->
        stmt[Recipe.id] = recipe.id
        stmt[Recipe.title] = recipe.title
        stmt[Recipe.description] = recipe.description
        stmt[Recipe.prepTime] = recipe.prepTime
        stmt[Recipe.cookingTime] = recipe.cookingTime
        stmt[Recipe.difficulty] = recipe.difficulty.name
        stmt[Recipe.image] = recipe.image
        stmt[Recipe.mealType] = recipe.mealType.name
        stmt[Recipe.kitchenStyle] = recipe.kitchenStyle.name
    }), RecipesRepository {

    override suspend fun findByTitle(title: String): List<RecipeEntry>  = transaction {
        Recipe.selectAll()
            .where { Recipe.title like title }
            .mapNotNull(toEntity)
    }

    override suspend fun findByDifficulty(difficulty: String): List<RecipeEntry> = transaction{
        Recipe.selectAll()
            .where(Recipe.difficulty eq difficulty)
            .mapNotNull(toEntity)
    }

    override suspend fun findByMealType(mealType: String): List<RecipeEntry> = transaction {
        Recipe.selectAll()
            .where(Recipe.mealType eq mealType)
            .mapNotNull(toEntity)
    }


    // Deze functie moet goed getest worden. Als dit werkt kunnen we op deze manier ook andere queries doen!!!
    // !!!
    override suspend fun findByDiets(diets: String): List<RecipeEntry> = transaction {
        (Recipe innerJoin RecipeDiet innerJoin Diet)
            .selectAll()
            .where(Diet.displayName eq diets)
            .map(toEntity)

    }

    override suspend fun findByKitchenStyle(kitchenStyle: String): List<RecipeEntry> = transaction {
        Recipe.selectAll()
            .where(Recipe.kitchenStyle eq kitchenStyle)
            .mapNotNull(toEntity)
    }

}
