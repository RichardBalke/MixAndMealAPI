package repository

import api.repository.CrudImplementation
import models.IngredientAllergens
import models.RecipeDiet
import models.RecipeDiets

interface RecipeDietsRepository {
}

class RecipeDietsRepositoryImpl() : CrudImplementation<RecipeDiet, RecipeDiet>(
    table = RecipeDiets,
    toEntity = {row ->
        RecipeDiet(row[RecipeDiets.recipeId], row[RecipeDiets.dietId])},
    idColumns = listOf(RecipeDiets.recipeId, RecipeDiets.dietId),
    idExtractor = { entry -> listOf(entry.recipeId, entry.dietId) },
    entityMapper = { stmt, recipeDiet ->
        stmt[RecipeDiets.recipeId] = recipeDiet.recipeId
        stmt[RecipeDiets.dietId] = recipeDiet.dietId
    }
), RecipeDietsRepository {}