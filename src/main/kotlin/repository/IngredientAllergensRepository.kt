package repository

import api.models.IngredientUnits
import api.repository.CrudImplementation
import models.IngredientAllergen
import models.IngredientAllergens

interface IngredientAllergensRepository {
}

class IngredientAllergensRepositoryImpl() : CrudImplementation<IngredientAllergen, Pair<String, String>>
    (
        table = IngredientAllergens,
        toEntity = { row ->
            IngredientAllergen(row[IngredientAllergens.ingredientName],
                row[IngredientAllergens.allergenId]) },
        idColumns = listOf(IngredientAllergens.ingredientName, IngredientAllergens.allergenId),
        idExtractor = { listOf(IngredientAllergens.ingredientName, IngredientAllergens.allergenId) },
        entityMapper = {stmt, ingredientAllergen ->
            stmt[IngredientAllergens.ingredientName] = ingredientAllergen.ingredientName
            stmt[IngredientAllergens.allergenId] = ingredientAllergen.allergenId
        }), IngredientAllergensRepository { }