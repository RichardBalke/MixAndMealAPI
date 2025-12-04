package repository

import api.repository.CrudImplementation
import models.dto.DietEntry
import models.tables.Diets

interface DietsRepository {
}

class DietsRepositoryImpl() : DietsRepository, CrudImplementation<DietEntry, Int>(
    table = Diets,
    toEntity = {row ->
        DietEntry(row[Diets.id], row[Diets.displayName], row[Diets.description])
    },
    idColumns = listOf(Diets.id),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = {stmt, diet ->
        stmt[Diets.id] = diet.id
        stmt[Diets.displayName] = diet.displayName
        stmt[Diets.description] = diet.description
    }
) {}