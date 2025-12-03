package repository

import api.repository.CrudImplementation
import models.dto.RecipeDietEntry
import models.tables.RecipeDiet

interface RecipeDietsRepository {
}

class RecipeDietsRepositoryImpl() : CrudImplementation<RecipeDietEntry, RecipeDietEntry>(
    table = RecipeDiet,
    toEntity = {row ->
        RecipeDietEntry(row[RecipeDiet.recipeId], row[RecipeDiet.dietId])},
    idColumns = listOf(RecipeDiet.recipeId, RecipeDiet.dietId),
    idExtractor = { entry -> listOf(entry.recipeId, entry.dietId) },
    entityMapper = { stmt, recipeDiet ->
        stmt[RecipeDiet.recipeId] = recipeDiet.recipeId
        stmt[RecipeDiet.dietId] = recipeDiet.dietId
    }
), RecipeDietsRepository {}