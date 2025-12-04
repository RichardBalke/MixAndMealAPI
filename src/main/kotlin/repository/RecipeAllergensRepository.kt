package repository

import api.repository.CrudImplementation
import models.dto.RecipeAllergenEntry
import models.tables.RecipeAllergens

interface IngredientAllergensRepository {
}

class IngredientAllergensRepositoryImpl() : CrudImplementation<RecipeAllergenEntry, RecipeAllergenEntry>
    (
        table = RecipeAllergens,
        toEntity = { row ->
            RecipeAllergenEntry(row[RecipeAllergens.recipeId],
                row[RecipeAllergens.allergenId]) },
        idColumns = listOf(RecipeAllergens.recipeId, RecipeAllergens.allergenId),
        idExtractor = { entry -> listOf(entry.recipeId, entry.allergenId)},
        entityMapper = {stmt, ingredientAllergen ->
            stmt[RecipeAllergens.recipeId] = ingredientAllergen.recipeId
            stmt[RecipeAllergens.allergenId] = ingredientAllergen.allergenId
        }), IngredientAllergensRepository { }