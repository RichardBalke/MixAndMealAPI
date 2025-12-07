package repository

import api.repository.CrudImplementation
import api.repository.CrudRepository
import models.dto.RecipeDietEntry
import models.tables.RecipeDiets
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface RecipeDietsRepository : CrudRepository<RecipeDietEntry, RecipeDietEntry> {
    suspend fun findAllByRecipeId(recipeId: Int): List<RecipeDietEntry>
}

class RecipeDietsRepositoryImpl() : CrudImplementation<RecipeDietEntry, RecipeDietEntry>(
    table = RecipeDiets,
    toEntity = {row ->
        RecipeDietEntry(row[RecipeDiets.recipeId], row[RecipeDiets.dietId])},
    idColumns = listOf(RecipeDiets.recipeId, RecipeDiets.dietId),
    idExtractor = { entry -> listOf(entry.recipeId, entry.dietId) },
    entityMapper = { stmt, recipeDiet ->
        stmt[RecipeDiets.recipeId] = recipeDiet.recipeId
        stmt[RecipeDiets.dietId] = recipeDiet.dietId
    }
), RecipeDietsRepository {

    override suspend fun findAllByRecipeId(recipeId : Int): List<RecipeDietEntry> = transaction {
        table.selectAll()
            .where{RecipeDiets.recipeId eq recipeId}
            .map(toEntity)
            .toList()
    }
}