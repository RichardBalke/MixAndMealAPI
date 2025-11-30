package repository

import api.models.Allergen
import api.models.Allergens
import api.repository.CrudImplementation

interface AllergensRepository {

}

class AllergensRepositoryImpl() : AllergensRepository, CrudImplementation<Allergen, Int>(
    table = Allergens,
    toEntity = { row -> Allergen(row[Allergens.id],
        row[Allergens.name],
        row[Allergens.description],
        row[Allergens.description]) },
    idColumns = listOf(Allergens.id),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = { stmt, allergen ->
        stmt[Allergens.id] = allergen.id
        stmt[Allergens.name] = allergen.name
        stmt[Allergens.displayName] = allergen.displayName
        stmt[Allergens.description] = allergen.description
    }
) {

}

