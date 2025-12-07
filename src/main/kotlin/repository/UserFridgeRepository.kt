package repository

import models.dto.UserFridgeEntry
import api.repository.CrudImplementation
import models.tables.UserFridge
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface UserFridgeRepository {
    suspend fun getFridgeForUser(userId: String): List<UserFridgeEntry>
    suspend fun addIngredient(userId: String, ingredientName: String): UserFridgeEntry
    suspend fun removeIngredient(userId: String, ingredientName: String)
}

class UserFridgeRepositoryImpl :
    UserFridgeRepository,
    CrudImplementation<UserFridgeEntry, UserFridgeEntry>(
        table = UserFridge,
        toEntity = { row ->
            UserFridgeEntry(
                userId = row[UserFridge.userId],
                ingredientName = row[UserFridge.ingredientName]
            )
        },
        idColumns = listOf(UserFridge.userId, UserFridge.ingredientName),
        idExtractor = { entry -> listOf(entry.userId, entry.ingredientName) },
        entityMapper = { stmt, entry ->
            stmt[UserFridge.userId] = entry.userId
            stmt[UserFridge.ingredientName] = entry.ingredientName
        }
    ) {

    override suspend fun getFridgeForUser(userId: String): List<UserFridgeEntry> = transaction {
        UserFridge
            .selectAll()
            .where(UserFridge.userId eq userId)
            .map(toEntity)
    }

    override suspend fun addIngredient(userId: String, ingredientName: String): UserFridgeEntry {
        val entry = UserFridgeEntry(userId, ingredientName)
        return create(entry)
    }

    override suspend fun removeIngredient(userId: String, ingredientName: String) {
        delete(UserFridgeEntry(userId, ingredientName))
    }
}


