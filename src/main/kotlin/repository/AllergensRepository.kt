package repository

import api.repository.CrudImplementation
import api.repository.CrudRepository
import models.dto.AllergenEntry
import models.tables.Allergens
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface AllergensRepository : CrudRepository<AllergenEntry, Int> {
    suspend fun findByDisplayName(displayName: String): AllergenEntry?  // ADD THIS
}
class AllergensRepositoryImpl() : AllergensRepository, CrudImplementation<AllergenEntry, Int>(
    table = Allergens,
    toEntity = { row ->
        AllergenEntry(
            row[Allergens.id],
            row[Allergens.name],
            row[Allergens.displayName],  // Fixed: was row[Allergens.description]
            row[Allergens.description]
        )
    },
    idColumns = listOf(Allergens.id),
    idExtractor = { entry -> listOf(entry) },
    entityMapper = { stmt, allergen ->
        stmt[Allergens.id] = allergen.id
        stmt[Allergens.name] = allergen.name
        stmt[Allergens.displayName] = allergen.displayName
        stmt[Allergens.description] = allergen.description
    }
) {
    override suspend fun findByDisplayName(displayName: String): AllergenEntry? = transaction {
        table.selectAll()
            .where(Allergens.displayName eq displayName).firstNotNullOfOrNull(toEntity)
    }
}
