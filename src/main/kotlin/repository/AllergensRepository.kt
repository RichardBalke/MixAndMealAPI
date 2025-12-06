package repository

import api.repository.CrudImplementation
import api.repository.CrudRepository
import models.dto.AllergenEntry
import models.tables.Allergens

interface AllergensRepository : CrudRepository<AllergenEntry, Int> {

}

class AllergensRepositoryImpl() : AllergensRepository, CrudImplementation<AllergenEntry, Int>(
    table = Allergens,
    toEntity = { row -> AllergenEntry(row[Allergens.id],
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

