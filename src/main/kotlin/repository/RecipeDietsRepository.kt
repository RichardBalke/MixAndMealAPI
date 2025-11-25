package repository

import api.repository.CrudImplementation
import models.IngredientAllergens
import models.RecipeDiet
import models.RecipeDiets

interface RecipeDietsRepository {
}

class RecipeDietsRepositoryImpl() : CrudImplementation<RecipeDiet, Pair<Int, Int>>(
    table = RecipeDiets,
    toEntity = {row ->
        RecipeDiet(row[RecipeDiets.recipeId], row[RecipeDiets.dietId])},
    idColumns = listOf(RecipeDiets.recipeId, RecipeDiets.dietId),
    idExtractor = { listOf(RecipeDiets.recipeId, RecipeDiets.dietId) },
    entityMapper = { stmt, recipeDiet ->
        stmt[RecipeDiets.recipeId] = recipeDiet.recipeId
        stmt[RecipeDiets.dietId] = recipeDiet.dietId
    }
), RecipeDietsRepository {}