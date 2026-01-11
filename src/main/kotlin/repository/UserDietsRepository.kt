package repository

import models.dto.UserDietEntry
import api.repository.CrudImplementation
import api.repository.CrudRepository
import models.dto.UserEntry
import models.tables.UserDiets
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface UserDietsRepository : CrudRepository<UserDietEntry, UserDietEntry> {
    suspend fun getDietsForUser(userId: String): List<UserDietEntry>
    suspend fun addDiet(userId: String, dietId: Int): UserDietEntry
    suspend fun removeDiet(userId: String, dietId: Int)
}

class UserDietsRepositoryImpl :
    UserDietsRepository,
    CrudImplementation<UserDietEntry, UserDietEntry>(
        table = UserDiets,
        toEntity = { row ->
            UserDietEntry(
                userId = row[UserDiets.userId],
                dietId = row[UserDiets.dietId]
            )
        },
        idColumns = listOf(UserDiets.userId, UserDiets.dietId),
        idExtractor = { entry -> listOf(entry.userId, entry.dietId) },
        entityMapper = { stmt, entry ->
            stmt[UserDiets.userId] = entry.userId
            stmt[UserDiets.dietId] = entry.dietId
        }
    ) {

    override suspend fun getDietsForUser(userId: String): List<UserDietEntry> = transaction {
        UserDiets
            .selectAll()
            .where(UserDiets.userId eq userId)
            .mapNotNull(toEntity)
            .toList()
    }

    override suspend fun addDiet(userId: String, dietId: Int): UserDietEntry {
        val entry = UserDietEntry(userId, dietId)
        return create(entry)
    }

    override suspend fun removeDiet(userId: String, dietId: Int) {
        delete(UserDietEntry(userId, dietId))
    }
}
