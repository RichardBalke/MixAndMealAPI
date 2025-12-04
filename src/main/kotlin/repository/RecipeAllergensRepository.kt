package repository

import api.repository.CrudImplementation
import models.dto.RecipeAllergenEntry
import models.tables.RecipeAllergen

interface IngredientAllergensRepository {
}

class IngredientAllergensRepositoryImpl() : CrudImplementation<RecipeAllergenEntry, RecipeAllergenEntry>
    (
        table = RecipeAllergen,
        toEntity = { row ->
            RecipeAllergenEntry(row[RecipeAllergen.recipeId],
                row[RecipeAllergen.allergenId]) },
        idColumns = listOf(RecipeAllergen.recipeId, RecipeAllergen.allergenId),
        idExtractor = { entry -> listOf(entry.recipeId, entry.allergenId)},
        entityMapper = {stmt, ingredientAllergen ->
            stmt[RecipeAllergen.recipeId] = ingredientAllergen.recipeId
            stmt[RecipeAllergen.allergenId] = ingredientAllergen.allergenId
        }), IngredientAllergensRepository { }