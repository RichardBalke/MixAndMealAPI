package api.repository


import api.models.IngredientUnit
import api.models.IngredientUnitId
import api.models.IngredientUnits
import api.models.Recipes
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import responses.RecipeSearchResult

interface IngredientUnitRepository {
 suspend fun findRecipesByIngredient(ingredientName : String): List<RecipeSearchResult>
}

class IngredientUnitRepositoryImpl() : IngredientUnitRepository,
    CrudImplementation<IngredientUnit, IngredientUnitId>(
    table = IngredientUnits,
    toEntity = { row ->
        IngredientUnit(row[IngredientUnits.recipeId],
            row[IngredientUnits.ingredientName],
            row[IngredientUnits.amount],
            row[IngredientUnits.unitType]
        )},
    idColumns = listOf(IngredientUnits.recipeId, IngredientUnits.ingredientName),
    idExtractor = {entry -> listOf(entry.recipeId, entry.ingredientName)},
    entityMapper = { stmt, ingredientUnit ->
        stmt[IngredientUnits.recipeId] = ingredientUnit.recipeId
        stmt[IngredientUnits.ingredientName] = ingredientUnit.ingredientName
        stmt[IngredientUnits.amount] = ingredientUnit.amount
        stmt[IngredientUnits.unitType] = ingredientUnit.unitType
    }
) {
    override suspend fun findRecipesByIngredient(ingredientName: String): List<RecipeSearchResult> {
        val recipeIds = transaction {
            IngredientUnits.selectAll()
                .where { IngredientUnits.ingredientName like ingredientName }
                .map { row -> row[IngredientUnits.recipeId] }
        }
        val foundRecipes = mutableListOf<RecipeSearchResult>()
        for (recipe in recipeIds) {
            val search = transaction {
                Recipes.selectAll()
                    .where { Recipes.id eq recipe }
                    .map { row ->
                        RecipeSearchResult(
                            row[Recipes.id],
                            row[Recipes.title],
                            row[Recipes.image]
                        )
                    }
            }
            foundRecipes.addAll(search)
        }
        return foundRecipes
    }
}
