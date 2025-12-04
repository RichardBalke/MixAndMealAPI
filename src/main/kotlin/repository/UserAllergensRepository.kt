package repository

import models.dto.UserAllergenEntry
import api.repository.CrudImplementation
import models.tables.UserAllergen
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

interface UserAllergensRepository {
    suspend fun getAllergensForUser(userId: String): List<UserAllergenEntry>
    suspend fun addAllergen(userId: String, allergenId: Int): UserAllergenEntry
    suspend fun removeAllergen(userId: String, allergenId: Int)
}

class UserAllergensRepositoryImpl :
    UserAllergensRepository,
    CrudImplementation<UserAllergenEntry, UserAllergenEntry>(
        table = UserAllergen,
        toEntity = { row ->
            UserAllergenEntry(
                userId = row[UserAllergen.userId],
                allergenId = row[UserAllergen.allergenId]
            )
        },
        idColumns = listOf(UserAllergen.userId, UserAllergen.allergenId),
        idExtractor = { entry -> listOf(entry.userId, entry.allergenId) },
        entityMapper = { stmt, entry ->
            stmt[UserAllergen.userId] = entry.userId
            stmt[UserAllergen.allergenId] = entry.allergenId
        }
    ) {

    override suspend fun getAllergensForUser(userId: String): List<UserAllergenEntry> = transaction {
        UserAllergen
            .select(UserAllergen.userId eq userId)
            .map(toEntity)
    }

    override suspend fun addAllergen(userId: String, allergenId: Int): UserAllergenEntry {
        val entry = UserAllergenEntry(userId, allergenId)
        return create(entry) // create() komt uit CrudImplementation
    }

    override suspend fun removeAllergen(userId: String, allergenId: Int) {
        delete(UserAllergenEntry(userId, allergenId))
    }
}