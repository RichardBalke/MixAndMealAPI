package api.repository

import models.dto.IngredientUnitEntry
import models.tables.IngredientUnit
import models.tables.Recipe
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import responses.RecipeSearchResult

interface IngredientUnitRepository {
 suspend fun findRecipesByIngredient(ingredientName : String): List<RecipeSearchResult>
}

class IngredientUnitRepositoryImpl() : IngredientUnitRepository,
    CrudImplementation<IngredientUnitEntry, IngredientUnitEntry>(
    table = IngredientUnit,
    toEntity = { row ->
        IngredientUnitEntry(row[IngredientUnit.recipeId],
            row[IngredientUnit.ingredientName],
            row[IngredientUnit.amount],
            row[IngredientUnit.unitType]
        )},
    idColumns = listOf(IngredientUnit.recipeId, IngredientUnit.ingredientName),
    idExtractor = {entry -> listOf(entry.recipeId, entry.ingredientName)},
    entityMapper = { stmt, ingredientUnit ->
        stmt[IngredientUnit.recipeId] = ingredientUnit.recipeId
        stmt[IngredientUnit.ingredientName] = ingredientUnit.ingredientName
        stmt[IngredientUnit.amount] = ingredientUnit.amount
        stmt[IngredientUnit.unitType] = ingredientUnit.unitType
    }
) {
    override suspend fun findRecipesByIngredient(ingredientName: String): List<RecipeSearchResult> {
        val recipeIds = transaction {
            IngredientUnit.selectAll()
                .where { IngredientUnit.ingredientName like ingredientName }
                .map { row -> row[IngredientUnit.recipeId] }
        }
        val foundRecipes = mutableListOf<RecipeSearchResult>()
        for (recipe in recipeIds) {
            val search = transaction {
                Recipe.selectAll()
                    .where { Recipe.id eq recipe }
                    .map { row ->
                        RecipeSearchResult(
                            row[Recipe.id],
                            row[Recipe.title],
                            row[Recipe.image]
                        )
                    }
            }
            foundRecipes.addAll(search)
        }
        return foundRecipes
    }
}
