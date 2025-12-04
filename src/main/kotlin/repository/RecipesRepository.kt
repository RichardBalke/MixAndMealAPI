package api.repository

import models.enums.Difficulty
import models.enums.KitchenStyle
import models.enums.MealType
import models.dto.RecipeEntry
import models.tables.Recipes
import models.tables.RecipeDiets
import models.tables.Diets
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

    override suspend fun findByTitle(title: String): List<RecipeEntry>  = transaction {
        Recipes.selectAll()
            .where { Recipes.title like title }
            .mapNotNull(toEntity)
    }

    override suspend fun findByDifficulty(difficulty: String): List<RecipeEntry> = transaction{
        Recipes.selectAll()
            .where(Recipes.difficulty eq difficulty)
            .mapNotNull(toEntity)
    }

    override suspend fun findByMealType(mealType: String): List<RecipeEntry> = transaction {
        Recipes.selectAll()
            .where(Recipes.mealType eq mealType)
            .mapNotNull(toEntity)
    }


    // Deze functie moet goed getest worden. Als dit werkt kunnen we op deze manier ook andere queries doen!!!
    // !!!
    override suspend fun findByDiets(diets: String): List<RecipeEntry> = transaction {
        (Recipes innerJoin RecipeDiets innerJoin Diets)
            .selectAll()
            .where(Diets.displayName eq diets)
            .map(toEntity)

    }

    override suspend fun findByKitchenStyle(kitchenStyle: String): List<RecipeEntry> = transaction {
        Recipes.selectAll()
            .where(Recipes.kitchenStyle eq kitchenStyle)
            .mapNotNull(toEntity)
    }

}
