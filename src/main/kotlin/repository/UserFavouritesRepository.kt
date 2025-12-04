package repository

import models.dto.UserFavouritesEntry
import api.repository.CrudImplementation
import models.tables.UserFavourite
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

interface UserFavouritesRepository {
    suspend fun getFavouritesForUser(userId: String): List<UserFavouritesEntry>
    suspend fun addFavourite(userId: String, recipeId: Int): UserFavouritesEntry
    suspend fun removeFavourite(userId: String, recipeId: Int)
}

class UserFavouritesRepositoryImpl :
    UserFavouritesRepository,
    CrudImplementation<UserFavouritesEntry, UserFavouritesEntry>(
        table = UserFavourite,
        toEntity = { row ->
            UserFavouritesEntry(
                userId = row[UserFavourite.userId],
                recipeId = row[UserFavourite.recipeId]
            )
        },
        idColumns = listOf(UserFavourite.userId, UserFavourite.recipeId),
        idExtractor = { entry -> listOf(entry.userId, entry.recipeId) },
        entityMapper = { stmt, entry ->
            stmt[UserFavourite.userId] = entry.userId
            stmt[UserFavourite.recipeId] = entry.recipeId
        }
    ) {

    override suspend fun getFavouritesForUser(userId: String): List<UserFavouritesEntry> = transaction {
        UserFavourite
            .select(UserFavourite.userId eq userId)
            .map(toEntity)
    }

    override suspend fun addFavourite(userId: String, recipeId: Int): UserFavouritesEntry {
        val entry = UserFavouritesEntry(userId, recipeId)
        return create(entry)
    }

    override suspend fun removeFavourite(userId: String, recipeId: Int) {
        delete(UserFavouritesEntry(userId, recipeId))
    }
}