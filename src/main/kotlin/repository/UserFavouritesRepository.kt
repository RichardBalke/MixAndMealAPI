package repository

import models.dto.UserFavouritesEntry
import api.repository.CrudImplementation
import api.repository.CrudRepository
import models.tables.UserFavourites
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface UserFavouritesRepository : CrudRepository<UserFavouritesEntry, UserFavouritesEntry> {
    suspend fun getFavouritesForUser(userId: String): List<UserFavouritesEntry>
    suspend fun addFavourite(userId: String, recipeId: Int): UserFavouritesEntry
    suspend fun removeFavourite(userId: String, recipeId: Int)
    suspend fun checkFavouriteExists(userId: String, recipeId: Int) : List<UserFavouritesEntry>
}

class UserFavouritesRepositoryImpl :
    UserFavouritesRepository,
    CrudImplementation<UserFavouritesEntry, UserFavouritesEntry>(
        table = UserFavourites,
        toEntity = { row ->
            UserFavouritesEntry(
                userId = row[UserFavourites.userId],
                recipeId = row[UserFavourites.recipeId]
            )
        },
        idColumns = listOf(UserFavourites.userId, UserFavourites.recipeId),
        idExtractor = { entry -> listOf(entry.userId, entry.recipeId) },
        entityMapper = { stmt, entry ->
            stmt[UserFavourites.userId] = entry.userId
            stmt[UserFavourites.recipeId] = entry.recipeId
        }
    ) {

    override suspend fun getFavouritesForUser(userId: String): List<UserFavouritesEntry> = transaction {
        UserFavourites
            .selectAll()
            .where(UserFavourites.userId eq userId)
            .map(toEntity)
            .toList()
    }

    override suspend fun addFavourite(userId: String, recipeId: Int): UserFavouritesEntry {
        val entry = UserFavouritesEntry(userId, recipeId)
        return create(entry)
    }

    override suspend fun removeFavourite(userId: String, recipeId: Int) {
        delete(UserFavouritesEntry(userId, recipeId))
    }

    override suspend fun checkFavouriteExists(userId: String, recipeId: Int): List<UserFavouritesEntry> = transaction {
        UserFavourites.selectAll()
            .where((UserFavourites.userId eq userId) and (UserFavourites.recipeId eq recipeId))
            .map(toEntity)
        .toList()
    }
}