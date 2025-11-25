package repository

import api.models.UserFavouritesEntry
import api.models.UserFavourites
import api.repository.CrudImplementation
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
            .select(UserFavourites.userId eq userId)
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