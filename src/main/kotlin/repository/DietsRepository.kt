package repository

import api.models.Diet
import api.models.Diets
import api.repository.CrudImplementation

interface DietsRepository {
}

class DietsRepositoryImpl() : DietsRepository, CrudImplementation<Diet, Int>(
    table = Diets,
    toEntity = {row ->
        Diet(row[Diets.id], row[Diets.displayName], row[Diets.description])
    },
    idColumns = listOf(Diets.id),
    idExtractor = {entry -> listOf(entry)},
    entityMapper = {stmt, diet ->
        stmt[Diets.id] = diet.id
        stmt[Diets.displayName] = diet.displayName
        stmt[Diets.description] = diet.description
    }
) {}