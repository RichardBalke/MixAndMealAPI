package repository

import api.repository.CrudImplementation
import models.dto.DietEntry
import models.tables.Diet

interface DietsRepository {
}

class DietsRepositoryImpl() : DietsRepository, CrudImplementation<DietEntry, Int>(
    table = Diet,
    toEntity = {row ->
        DietEntry(row[Diet.id], row[Diet.displayName], row[Diet.description])
    },
    idColumns = listOf(Diet.id),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = {stmt, diet ->
        stmt[Diet.id] = diet.id
        stmt[Diet.displayName] = diet.displayName
        stmt[Diet.description] = diet.description
    }
) {}