package repository

import api.repository.CrudImplementation
import models.dto.AllergenEntry
import models.tables.Allergen

interface AllergensRepository {

}

class AllergensRepositoryImpl() : AllergensRepository, CrudImplementation<AllergenEntry, Int>(
    table = Allergen,
    toEntity = { row -> AllergenEntry(row[Allergen.id],
        row[Allergen.name],
        row[Allergen.description],
        row[Allergen.description]) },
    idColumns = listOf(Allergen.id),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = { stmt, allergen ->
        stmt[Allergen.id] = allergen.id
        stmt[Allergen.name] = allergen.name
        stmt[Allergen.displayName] = allergen.displayName
        stmt[Allergen.description] = allergen.description
    }
) {

}

