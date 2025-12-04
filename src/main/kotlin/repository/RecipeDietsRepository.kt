package repository

import api.repository.CrudImplementation
import models.dto.RecipeDietEntry
import models.tables.RecipeDiets

interface RecipeDietsRepository {
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
), RecipeDietsRepository {}