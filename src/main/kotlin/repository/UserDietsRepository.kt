package repository

import models.dto.UserDietEntry
import api.repository.CrudImplementation
import models.tables.UserDiet
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

interface UserDietsRepository {
    suspend fun getDietsForUser(userId: String): List<UserDietEntry>
    suspend fun addDiet(userId: String, dietId: Int): UserDietEntry
    suspend fun removeDiet(userId: String, dietId: Int)
}

class UserDietsRepositoryImpl :
    UserDietsRepository,
    CrudImplementation<UserDietEntry, UserDietEntry>(
        table = UserDiet,
        toEntity = { row ->
            UserDietEntry(
                userId = row[UserDiet.userId],
                dietId = row[UserDiet.dietId]
            )
        },
        idColumns = listOf(UserDiet.userId, UserDiet.dietId),
        idExtractor = { entry -> listOf(entry.userId, entry.dietId) },
        entityMapper = { stmt, entry ->
            stmt[UserDiet.userId] = entry.userId
            stmt[UserDiet.dietId] = entry.dietId
        }
    ) {

    override suspend fun getDietsForUser(userId: String): List<UserDietEntry> = transaction {
        UserDiet
            .select(UserDiet.userId eq userId)
            .map(toEntity)
    }

    override suspend fun addDiet(userId: String, dietId: Int): UserDietEntry {
        val entry = UserDietEntry(userId, dietId)
        return create(entry)
    }

    override suspend fun removeDiet(userId: String, dietId: Int) {
        delete(UserDietEntry(userId, dietId))
    }
}
