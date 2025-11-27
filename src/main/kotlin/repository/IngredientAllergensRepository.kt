package repository

import api.repository.CrudImplementation
import models.IngredientAllergen
import models.IngredientAllergens

interface IngredientAllergensRepository {
}

class IngredientAllergensRepositoryImpl() : CrudImplementation<IngredientAllergen, IngredientAllergen>
    (
        table = IngredientAllergens,
        toEntity = { row ->
            IngredientAllergen(row[IngredientAllergens.ingredientName],
                row[IngredientAllergens.allergenId]) },
        idColumns = listOf(IngredientAllergens.ingredientName, IngredientAllergens.allergenId),
        idExtractor = { entry -> listOf(entry.ingredientName, entry.allergenId)},
        entityMapper = {stmt, ingredientAllergen ->
            stmt[IngredientAllergens.ingredientName] = ingredientAllergen.ingredientName
            stmt[IngredientAllergens.allergenId] = ingredientAllergen.allergenId
        }), IngredientAllergensRepository { }