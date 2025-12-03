package repository

import api.repository.CrudImplementation
import models.dto.IngredientAllergenEntry
import models.tables.IngredientAllergen

interface IngredientAllergensRepository {
}

class IngredientAllergensRepositoryImpl() : CrudImplementation<IngredientAllergenEntry, IngredientAllergenEntry>
    (
        table = IngredientAllergen,
        toEntity = { row ->
            IngredientAllergenEntry(row[IngredientAllergen.ingredientName],
                row[IngredientAllergen.allergenId]) },
        idColumns = listOf(IngredientAllergen.ingredientName, IngredientAllergen.allergenId),
        idExtractor = { entry -> listOf(entry.ingredientName, entry.allergenId)},
        entityMapper = {stmt, ingredientAllergen ->
            stmt[IngredientAllergen.ingredientName] = ingredientAllergen.ingredientName
            stmt[IngredientAllergen.allergenId] = ingredientAllergen.allergenId
        }), IngredientAllergensRepository { }